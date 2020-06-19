package com.paypay.exchangerates.datasource.disk

import com.paypay.common.datasource.room.*
import com.paypay.exchangerates.data.disk.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import javax.inject.Inject

class ExchangeRatesRoom @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val rateDao: RateDao
) : ExchangeRatesDiskDataSource {

    override suspend fun getCurrencies(): List<Currency>? {
        val currencies = currencyDao.getAll()
        if (currencies.isNullOrEmpty()) return null
        return currencies.map { Currency(code = it.code, label = it.label) }
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencyDao.insert(currencies.map {
            CurrencyRoomEntity(
                code = it.code,
                label = it.label
            )
        })
    }

    override suspend fun getRatesByCurrency(currency: CurrencyCode): List<ExchangeRate>? {
        val rates = rateDao.getAllFromCurrency(currency) ?: return null
        return rates.rates.map { ExchangeRate(currencyCode = it.currency, rate = it.rate) }
    }

    override suspend fun saveRatesByCurrency(currency: CurrencyCode, rates: List<ExchangeRate>) {
        rateDao.insert(
            RateRoomEntity(
                source = currency,
                rates = rates.map {
                    RateEntity(
                        currency = it.currencyCode,
                        rate = it.rate
                    )
                }
            )
        )
    }
}