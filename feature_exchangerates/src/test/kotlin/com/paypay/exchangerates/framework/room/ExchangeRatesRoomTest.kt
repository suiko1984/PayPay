package com.paypay.exchangerates.framework.room

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.only
import com.nhaarman.mockitokotlin2.then
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class ExchangeRatesRoomTest {

    @Mock
    lateinit var currencyDao: CurrencyDao

    @Mock
    lateinit var rateDao: RateDao

    @InjectMocks
    lateinit var exchangeRatesRoom: ExchangeRatesRoom

    @Test
    fun `getCurrencies when normal case`() = runBlocking {
        given(currencyDao.getAll()).willReturn(
            listOf(
                CurrencyRoomEntity(code = "EUR", label = "Euro"),
                CurrencyRoomEntity(code = "USD", label = "US Dollar"),
                CurrencyRoomEntity(code = "JPY", label = "Japanese Yen"),
                CurrencyRoomEntity(code = "GBP", label = "British Pound")
            )
        )
        val result = exchangeRatesRoom.getCurrencies()
        assert(
            result == listOf(
                Currency("EUR", "Euro"),
                Currency("USD", "US Dollar"),
                Currency("JPY", "Japanese Yen"),
                Currency("GBP", "British Pound")
            )
        )
    }

    @Test
    fun `getCurrencies when room return null, should return null`() = runBlocking {
        given(currencyDao.getAll()).willReturn(null)
        val result = exchangeRatesRoom.getCurrencies()
        assert(result == null)
    }

    @Test
    fun saveCurrencies() = runBlocking {
        exchangeRatesRoom.saveCurrencies(
            listOf(
                Currency("EUR", "Euro"),
                Currency("USD", "US Dollar"),
                Currency("JPY", "Japanese Yen"),
                Currency("GBP", "British Pound")
            )
        )
        then(currencyDao).should(only()).insert(
            listOf(
                CurrencyRoomEntity(code = "EUR", label = "Euro"),
                CurrencyRoomEntity(code = "USD", label = "US Dollar"),
                CurrencyRoomEntity(code = "JPY", label = "Japanese Yen"),
                CurrencyRoomEntity(code = "GBP", label = "British Pound")
            )
        )
    }

    @Test
    fun `getRatesByCurrency when normal case`() = runBlocking {
        given(rateDao.getAllFromCurrency("EUR")).willReturn(
            RateRoomEntity(
                "EUR",
                listOf(
                    RateEntity(currency = "EUR", rate = 1.0),
                    RateEntity(currency = "USD", rate = 1.1254962031385587),
                    RateEntity(currency = "JPY", rate = 120.66332244228175),
                    RateEntity(currency = "GBP", rate = 0.8993547530267406)
                )
            )
        )
        val result = exchangeRatesRoom.getRatesByCurrency("EUR")
        assert(
            result == listOf(
                ExchangeRate(currencyCode = "EUR", rate = 1.0),
                ExchangeRate(currencyCode = "USD", rate = 1.1254962031385587),
                ExchangeRate(currencyCode = "JPY", rate = 120.66332244228175),
                ExchangeRate(currencyCode = "GBP", rate = 0.8993547530267406)
            )
        )
    }

    @Test
    fun `getRatesByCurrency when room return null, should return null`() = runBlocking {
        given(rateDao.getAllFromCurrency("EUR")).willReturn(null)
        val result = exchangeRatesRoom.getRatesByCurrency("EUR")
        assert(result == null)
    }

    @Test
    fun saveRatesByCurrency() = runBlocking {
        exchangeRatesRoom.saveRatesByCurrency(
            "EUR",
            listOf(
                ExchangeRate(currencyCode = "EUR", rate = 1.0),
                ExchangeRate(currencyCode = "USD", rate = 1.1254962031385587),
                ExchangeRate(currencyCode = "JPY", rate = 120.66332244228175),
                ExchangeRate(currencyCode = "GBP", rate = 0.8993547530267406)
            )
        )
        then(rateDao).should(only()).insert(
            RateRoomEntity(
                "EUR",
                listOf(
                    RateEntity(currency = "EUR", rate = 1.0),
                    RateEntity(currency = "USD", rate = 1.1254962031385587),
                    RateEntity(currency = "JPY", rate = 120.66332244228175),
                    RateEntity(currency = "GBP", rate = 0.8993547530267406)
                )
            )
        )
    }
}