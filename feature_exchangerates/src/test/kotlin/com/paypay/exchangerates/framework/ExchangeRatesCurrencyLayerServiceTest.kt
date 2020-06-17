package com.paypay.exchangerates.framework

import com.nhaarman.mockitokotlin2.mock
import com.paypay.exchangerates.data.network.CurrencyLayerService
import com.paypay.exchangerates.data.network.ExchangeRatesCurrencyLayerService
import com.paypay.exchangerates.data.network.model.CurrencyLayerErrorDataModel
import com.paypay.exchangerates.data.network.model.GetCurrenciesResponseDataModel
import com.paypay.exchangerates.data.network.model.GetExchangeRatesResponseDataModel
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.GetCurrenciesException
import com.paypay.exchangerates.domain.repository.GetRatesByCurrencyException
import com.paypay.exchangerates.domain.repository.RestrictedAccessException
import com.paypay.exchangerates.domain.repository.UnauthenticatedExeption
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
internal class ExchangeRatesCurrencyLayerServiceTest {

    @Mock
    lateinit var service: CurrencyLayerService

    @InjectMocks
    lateinit var dataSource: ExchangeRatesCurrencyLayerService

    @Test
    fun `getCurrencies when normal case, should return currency list`() {
        val call: Call<GetCurrenciesResponseDataModel> = mock()
        val response: Response<GetCurrenciesResponseDataModel> = mock()
        given(service.getCurrencies()).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(
            GetCurrenciesResponseDataModel(
                success = true,
                currencies = mapOf(
                    "EUR" to "Euro",
                    "USD" to "US Dollar",
                    "JPY" to "Japanese Yen",
                    "GBP" to "British Pound"
                )
            )
        )
        val result = dataSource.getCurrencies()
        assert(
            result == listOf(
                Currency("EUR", "Euro"),
                Currency("USD", "US Dollar"),
                Currency("JPY", "Japanese Yen"),
                Currency("GBP", "British Pound")
            )
        )
    }

    @Test(expected = GetCurrenciesException::class)
    fun `getCurrencies when server throws IOException, should expect GetCurrenciesException`() {
        val call: Call<GetCurrenciesResponseDataModel> = mock()
        given(service.getCurrencies()).willReturn(call)
        given(call.execute()).willThrow(IOException("exception"))
        dataSource.getCurrencies()
    }

    @Test(expected = GetCurrenciesException::class)
    fun `getCurrencies when server return null body, should expect GetCurrenciesException`() {
        val call: Call<GetCurrenciesResponseDataModel> = mock()
        val response: Response<GetCurrenciesResponseDataModel> = mock()
        given(service.getCurrencies()).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(null)
        dataSource.getCurrencies()
    }

    @Test(expected = GetCurrenciesException::class)
    fun `getCurrencies when server return null currencies, should expect GetCurrenciesException`() {
        val call: Call<GetCurrenciesResponseDataModel> = mock()
        val response: Response<GetCurrenciesResponseDataModel> = mock()
        given(service.getCurrencies()).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(GetCurrenciesResponseDataModel(success = true))
        dataSource.getCurrencies()
    }

    @Test(expected = UnauthenticatedExeption::class)
    fun `getCurrencies when server return missing_access_key, should expect UnauthenticatedExeption`() {
        val call: Call<GetCurrenciesResponseDataModel> = mock()
        val response: Response<GetCurrenciesResponseDataModel> = mock()
        given(service.getCurrencies()).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(
            GetCurrenciesResponseDataModel(
                success = false,
                error = CurrencyLayerErrorDataModel(101, "")
            )
        )
        dataSource.getCurrencies()
    }

    @Test
    fun `getRatesByCurrency when normal case, should return exchange rate list`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        val response: Response<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("USD")).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(
            GetExchangeRatesResponseDataModel(
                success = true,
                quotes = mapOf(
                    "USDEUR" to 0.8894,
                    "USDUSD" to 1.0,
                    "USDJPY" to 107.474999,
                    "USDGBP" to 0.79949
                )
            )
        )
        val result = dataSource.getRatesByCurrency("USD")
        assert(
            result == listOf(
                ExchangeRate("EUR", 0.8894),
                ExchangeRate("USD", 1.0),
                ExchangeRate("JPY", 107.474999),
                ExchangeRate("GBP", 0.79949)
            )
        )
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when server throws IOException, should expect GetRatesByCurrencyException`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("EUR")).willReturn(call)
        given(call.execute()).willThrow(IOException("exception"))
        dataSource.getRatesByCurrency("EUR")
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when server return null body, should expect GetRatesByCurrencyException`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        val response: Response<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("EUR")).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(null)
        dataSource.getRatesByCurrency("EUR")
    }

    @Test(expected = GetRatesByCurrencyException::class)
    fun `getRatesByCurrency when server return null quotes, should expect GetRatesByCurrencyException`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        val response: Response<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("EUR")).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(GetExchangeRatesResponseDataModel(success = true))
        dataSource.getRatesByCurrency("EUR")
    }

    @Test(expected = UnauthenticatedExeption::class)
    fun `getRatesByCurrency when server return missing_access_key, should expect UnauthenticatedExeption`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        val response: Response<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("EUR")).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(
            GetExchangeRatesResponseDataModel(
                success = false,
                error = CurrencyLayerErrorDataModel(101, "")
            )
        )
        dataSource.getRatesByCurrency("EUR")
    }

    @Test(expected = RestrictedAccessException::class)
    fun `getRatesByCurrency when server throw RestrictedAccessException, should expect RestrictedAccessException`() {
        val call: Call<GetExchangeRatesResponseDataModel> = mock()
        val response: Response<GetExchangeRatesResponseDataModel> = mock()
        given(service.getExchangeRatesByCurrency("EUR")).willReturn(call)
        given(call.execute()).willReturn(response)
        given(response.body()).willReturn(
            GetExchangeRatesResponseDataModel(
                success = false,
                error = CurrencyLayerErrorDataModel(105, "")
            )
        )
        dataSource.getRatesByCurrency("EUR")
    }
}