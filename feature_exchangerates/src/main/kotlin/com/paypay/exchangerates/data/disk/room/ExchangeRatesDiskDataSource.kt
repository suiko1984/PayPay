package com.paypay.exchangerates.data.disk.room

import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate

interface ExchangeRatesDiskDataSource {
    suspend fun getCurrencies(): List<Currency>?
    suspend fun saveCurrencies(currencies: List<Currency>)
    suspend fun getRatesByCurrency(currency: CurrencyCode): List<ExchangeRate>?
    suspend fun saveRatesByCurrency(currency: CurrencyCode, rates: List<ExchangeRate>)
}