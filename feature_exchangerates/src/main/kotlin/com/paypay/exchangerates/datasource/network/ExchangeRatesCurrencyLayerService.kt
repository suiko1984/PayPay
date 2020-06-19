package com.paypay.exchangerates.datasource.network

import com.paypay.exchangerates.data.network.ExchangeRatesNetworkDataSource
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.GetCurrenciesException
import com.paypay.exchangerates.domain.repository.GetRatesByCurrencyException
import com.paypay.exchangerates.domain.repository.RestrictedAccessException
import com.paypay.exchangerates.domain.repository.UnauthenticatedExeption
import javax.inject.Inject

class ExchangeRatesCurrencyLayerService @Inject constructor(
    private val currencyLayerService: CurrencyLayerService
) : ExchangeRatesNetworkDataSource {

    override fun getCurrencies(): List<Currency> =
        runCatching {
            val response = currencyLayerService.getCurrencies().execute().body()
            if (response?.success == true) {
                response.currencies?.map { Currency(code = it.key, label = it.value) }
                    ?: throw GetCurrenciesException("Could not retrieve currencies")
            } else {
                val error = response?.error
                when (error?.code) {
                    101 -> throw UnauthenticatedExeption("${error.type} ${error.info}")
                    else -> throw GetCurrenciesException(error?.info)
                }
            }
        }.onFailure {
            when (it) {
                is UnauthenticatedExeption -> throw UnauthenticatedExeption(it.message)
                else -> throw GetCurrenciesException(it.message)
            }
        }.getOrThrow()

    override fun getRatesByCurrency(currency: String): List<ExchangeRate> =
        runCatching {
            val response = currencyLayerService
                .getExchangeRatesByCurrency(currency)
                .execute()
                .body()
            if (response?.success == true) {
                response.quotes?.map {
                    ExchangeRate(currencyCode = it.key.substringAfter(currency), rate = it.value)
                } ?: throw GetRatesByCurrencyException("Could not retrieve exchange rates")
            } else {
                val error = response?.error
                when (error?.code) {
                    101 -> throw UnauthenticatedExeption("${error.type} ${error.info}")
                    105 -> throw RestrictedAccessException(error.info)
                    else -> throw GetRatesByCurrencyException("${error?.type} ${error?.info}")
                }
            }
        }.onFailure {
            when (it) {
                is UnauthenticatedExeption -> throw UnauthenticatedExeption(it.message)
                is RestrictedAccessException -> throw RestrictedAccessException(it.message)
                else -> throw GetRatesByCurrencyException(it.message)
            }
        }.getOrThrow()
}