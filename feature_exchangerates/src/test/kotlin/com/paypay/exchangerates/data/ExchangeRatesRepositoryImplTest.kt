package com.paypay.exchangerates.data

import com.paypay.exchangerates.data.disk.room.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.data.disk.sharedpreferences.ExchangeRatesRefreshRulesDiskDataSource
import com.paypay.exchangerates.data.network.*
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.GetCurrenciesException
import com.paypay.exchangerates.domain.repository.GetRatesByCurrencyException
import com.paypay.exchangerates.domain.repository.RestrictedAccessException
import com.paypay.exchangerates.domain.repository.UnauthenticatedExeption
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class ExchangeRatesRepositoryImplTest {

    @Mock
    lateinit var rules: ExchangeRatesRefreshRulesDiskDataSource

    @Mock
    lateinit var diskDataSource: ExchangeRatesDiskDataSource

    @Mock
    lateinit var networkDataSource: ExchangeRatesNetworkDataSource

    @InjectMocks
    lateinit var repository: ExchangeRatesRepositoryImpl

    @Test
    fun `getCurrencies when disk cache is not empty, should return a list of currencies from this cache`() =
        runBlocking {
            val currencies = listOf(
                Currency("EUR", "Euro"),
                Currency("USD", "US Dollar"),
                Currency("JPY", "Japanese Yen"),
                Currency("GBP", "British Pound")
            )
            given(diskDataSource.getCurrencies()).willReturn(currencies)
            val result = repository.getCurrencies()
            then(diskDataSource).should(never()).saveCurrencies(currencies)
            assert(result == currencies)
        }

    @Test
    fun `getCurrencies when disk cache is empty, should return a list of currencies from the server`() =
        runBlocking {
            val currencies = listOf(
                Currency("EUR", "Euro"),
                Currency("USD", "US Dollar"),
                Currency("JPY", "Japanese Yen"),
                Currency("GBP", "British Pound")
            )
            given(diskDataSource.getCurrencies()).willReturn(null)
            given(networkDataSource.getCurrencies()).willReturn(currencies)
            val result = repository.getCurrencies()
            then(diskDataSource).should().saveCurrencies(currencies)
            assert(result == currencies)
        }

    @Test(expected = UnauthenticatedExeption::class)
    fun `getCurrencies when disk cache is empty and data server throws UnauthenticatedExeption, should expect UnauthenticatedExeption`() {
        runBlocking {
            given(diskDataSource.getCurrencies()).willReturn(null)
            given(networkDataSource.getCurrencies()).willThrow(
                UnauthenticatedExeption(
                    "Exception"
                )
            )
            repository.getCurrencies()
        }
    }

    @Test(expected = GetCurrenciesException::class)
    fun `getCurrencies when disk cache is empty and data server throws GetCurrenciesException, should expect GetCurrenciesException`() {
        runBlocking {
            given(diskDataSource.getCurrencies()).willReturn(null)
            given(networkDataSource.getCurrencies()).willThrow(
                GetCurrenciesException(
                    "Exception"
                )
            )
            repository.getCurrencies()
        }
    }

    @Test
    fun `getRatesByCurrency when data should not be refreshed, should return data from the disk cache`() =
        runBlocking {
            val rates = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 1.0
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.12412
                ),
                ExchangeRate(
                    "JPY",
                    rate = 120.784
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.898827
                )
            )
            given(rules.shouldUpdate()).willReturn(false)
            given(diskDataSource.getRatesByCurrency("EUR")).willReturn(rates)
            val result = repository.getRatesByCurrency("EUR")
            assert(result == rates)
        }

    @Test
    fun `getRatesByCurrency when data should be refreshed, should return data from the server`() =
        runBlocking {
            val rates = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 1.0
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.12412
                ),
                ExchangeRate(
                    "JPY",
                    rate = 120.784
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.898827
                )
            )
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willReturn(rates)
            val result = repository.getRatesByCurrency("EUR")
            then(diskDataSource).should().saveRatesByCurrency("EUR", rates)
            then(rules).should().update()
            assert(result == rates)
        }

    @Test(expected = UnauthenticatedExeption::class)
    fun `getRatesByCurrency when data should be refreshed and rates from server throws UnauthenticatedExeption, should expect UnauthenticatedExeption`() {
        runBlocking {
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willThrow(
                UnauthenticatedExeption(
                    "Exception"
                )
            )
            repository.getRatesByCurrency("EUR")
        }
    }

    @Test
    fun `getRatesByCurrency when data should be refreshed and rates from server throws RestrictedAccessException, should convert USD rates to currency rates`() {
        runBlocking {
            val usdRates = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 0.888497
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.0
                ),
                ExchangeRate(
                    "JPY",
                    rate = 107.209
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.799074
                )
            )
            val convertedEurRatesFromUsd = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 1.0
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.1254962031385587
                ),
                ExchangeRate(
                    "JPY",
                    rate = 120.66332244228175
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.8993547530267406
                )
            )
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willThrow(
                RestrictedAccessException(
                    "Exception"
                )
            )
            given(networkDataSource.getRatesByCurrency("USD")).willReturn(usdRates)
            val result = repository.getRatesByCurrency("EUR")
            then(diskDataSource).should().saveRatesByCurrency("USD", usdRates)
            then(rules).should().update()
            then(diskDataSource).should().saveRatesByCurrency("EUR", convertedEurRatesFromUsd)
            assert(result == convertedEurRatesFromUsd)
        }
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when data should be refreshed and rates from server throws RestrictedAccessException and USD rates from server also throws an exception, should expect GetRatesByCurrencyException`() {
        runBlocking {
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willThrow(
                RestrictedAccessException(
                    "Exception"
                )
            )
            given(networkDataSource.getRatesByCurrency("USD")).willThrow(
                RestrictedAccessException(
                    "Exception"
                )
            )
            repository.getRatesByCurrency("EUR")
        }
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when data should be refreshed and rates from server throws RestrictedAccessException, should convert USD rates to currency rates with failure`() {
        runBlocking {
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willThrow(
                RestrictedAccessException(
                    "Exception"
                )
            )
            given(networkDataSource.getRatesByCurrency("USD")).willThrow(
                GetRatesByCurrencyException(
                    "Exception"
                )
            )
            repository.getRatesByCurrency("EUR")
        }
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when data should be refreshed and rates from server throws other exceptions, should expect GetRatesByCurrencyException`() {
        runBlocking {
            given(rules.shouldUpdate()).willReturn(true)
            given(networkDataSource.getRatesByCurrency("EUR")).willThrow(
                GetRatesByCurrencyException(
                    "Exception"
                )
            )
            repository.getRatesByCurrency("EUR")
        }
    }

    @Test
    fun `getRatesByCurrency when data should not be refreshed and rates from disk does not exist, should convert USD rates to currency rates`() {
        runBlocking {
            val usdRates = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 0.888497
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.0
                ),
                ExchangeRate(
                    "JPY",
                    rate = 107.209
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.799074
                )
            )
            val convertedEurRatesFromUsd = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 1.0
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.1254962031385587
                ),
                ExchangeRate(
                    "JPY",
                    rate = 120.66332244228175
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.8993547530267406
                )
            )
            given(rules.shouldUpdate()).willReturn(false)
            given(diskDataSource.getRatesByCurrency("EUR")).willReturn(null)
            given(diskDataSource.getRatesByCurrency("USD")).willReturn(usdRates)
            val result = repository.getRatesByCurrency("EUR")
            then(rules).should(never()).update()
            then(diskDataSource).should().saveRatesByCurrency("EUR", convertedEurRatesFromUsd)
            assert(result == convertedEurRatesFromUsd)
        }
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when data should not be refreshed and rates from disk does not exist and USD rates from disk does not contain currency, should expect GetRatesByCurrencyException`() {
        runBlocking {
            val usdRates = listOf(
                ExchangeRate(
                    "EUR",
                    rate = 0.888497
                ),
                ExchangeRate(
                    "USD",
                    rate = 1.0
                ),
                ExchangeRate(
                    "JPY",
                    rate = 107.209
                ),
                ExchangeRate(
                    "GBP",
                    rate = 0.799074
                )
            )
            given(rules.shouldUpdate()).willReturn(false)
            given(diskDataSource.getRatesByCurrency("UNK")).willReturn(null)
            given(diskDataSource.getRatesByCurrency("USD")).willReturn(usdRates)
            repository.getRatesByCurrency("UNK")
        }
    }
}