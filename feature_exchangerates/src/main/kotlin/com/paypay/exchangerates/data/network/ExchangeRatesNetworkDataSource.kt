package com.paypay.exchangerates.data.network

import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.GetCurrenciesException
import com.paypay.exchangerates.domain.repository.GetRatesByCurrencyException
import com.paypay.exchangerates.domain.repository.RestrictedAccessException
import com.paypay.exchangerates.domain.repository.UnauthenticatedExeption

interface ExchangeRatesNetworkDataSource {
    @Throws(UnauthenticatedExeption::class, GetCurrenciesException::class)
    fun getCurrencies(): List<Currency>

    @Throws(
        UnauthenticatedExeption::class,
        RestrictedAccessException::class,
        GetRatesByCurrencyException::class
    )
    fun getRatesByCurrency(currency: String): List<ExchangeRate>
}