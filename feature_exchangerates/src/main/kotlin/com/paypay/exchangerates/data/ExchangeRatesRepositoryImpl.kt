package com.paypay.exchangerates.data

import com.paypay.exchangerates.data.disk.room.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.data.disk.sharedpreferences.ExchangeRatesRefreshRulesDiskDataSource
import com.paypay.exchangerates.data.network.ExchangeRatesNetworkDataSource
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.*
import javax.inject.Inject

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val refreshRules: ExchangeRatesRefreshRulesDiskDataSource,
    private val diskDataSource: ExchangeRatesDiskDataSource,
    private val networkDataSource: ExchangeRatesNetworkDataSource
) : ExchangeRatesRepository {

    override suspend fun getCurrencies(): List<Currency> =
        diskDataSource.getCurrencies()
            ?: runCatching { networkDataSource.getCurrencies() }.onFailure {
                when (it) {
                    is UnauthenticatedExeption -> throw UnauthenticatedExeption(it.message)
                    else -> throw GetCurrenciesException(it.message)
                }
            }.onSuccess {
                diskDataSource.saveCurrencies(it)
            }.getOrThrow()

    override suspend fun getRatesByCurrency(currency: CurrencyCode): List<ExchangeRate> =
        if (refreshRules.shouldUpdate()) {
            runCatching { networkDataSource.getRatesByCurrency(currency) }
                .onFailure {
                    when (it) {
                        is UnauthenticatedExeption -> throw UnauthenticatedExeption(it.message)
                        is RestrictedAccessException -> {
                            return getUSDRatesAndConvertToCurrency(currency)
                        }
                        else -> throw GetRatesByCurrencyException(it.message)
                    }
                }.onSuccess {
                    diskDataSource.saveRatesByCurrency(currency, it)
                    refreshRules.update()
                }.getOrThrow()
        } else {
            diskDataSource.getRatesByCurrency(currency) ?: getUSDRatesAndConvertToCurrency(currency)
        }

    /**
     * Special requirement:
     * "for currencies that are not available, convert them on the app side."
     */
    private suspend fun getUSDRatesAndConvertToCurrency(currency: CurrencyCode): List<ExchangeRate> =
        runCatching {
            if (currency == DEFAULT_CURRENCY_CODE) throw GetRatesByCurrencyException(
                "Could not get USD rates"
            )
            val usdRates = getRatesByCurrency(DEFAULT_CURRENCY_CODE)
            val currencyRate = usdRates.find { it.currencyCode == currency }?.rate
                ?: throw GetRatesByCurrencyException("Could not convert locally")
            usdRates.map { ExchangeRate(it.currencyCode, it.rate / currencyRate) }
        }.onSuccess {
            diskDataSource.saveRatesByCurrency(currency, it)
        }.onFailure {
            throw GetRatesByCurrencyException(it.message)
        }.getOrThrow()

    companion object {
        const val DEFAULT_CURRENCY_CODE = "USD"
    }
}