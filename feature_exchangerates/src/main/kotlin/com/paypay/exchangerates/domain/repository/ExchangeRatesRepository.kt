package com.paypay.exchangerates.domain.repository

import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate

interface ExchangeRatesRepository {
    @Throws(UnauthenticatedExeption::class, GetCurrenciesException::class)
    suspend fun getCurrencies(): List<Currency>

    @Throws(
        UnauthenticatedExeption::class,
        RestrictedAccessException::class,
        GetRatesByCurrencyException::class
    )
    suspend fun getRatesByCurrency(currency: String): List<ExchangeRate>
}

class UnauthenticatedExeption(override val message: String? = null) : Exception()
class RestrictedAccessException(override val message: String? = null) : Exception()
class GetCurrenciesException(override val message: String? = null) : Exception()
class GetRatesByCurrencyException(override val message: String? = null) : Exception()