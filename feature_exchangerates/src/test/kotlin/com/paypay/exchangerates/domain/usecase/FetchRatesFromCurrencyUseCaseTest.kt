package com.paypay.exchangerates.domain.usecase

import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import com.paypay.exchangerates.domain.repository.GetRatesByCurrencyException
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class FetchRatesFromCurrencyUseCaseTest {

    @Mock
    lateinit var exchangeRatesRepository: ExchangeRatesRepository

    @InjectMocks
    lateinit var useCase: FetchRatesFromCurrencyUseCase

    @Test
    fun `fetchRatesFromCurrency with normal case`() = runBlocking {
        val list = listOf(
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
        given(exchangeRatesRepository.getRatesByCurrency("EUR")).willReturn(list)
        val result = useCase.invoke("EUR")
        assert(result == list)
    }

    @Test(expected = FetchRatesFromCurrencyException::class)
    fun `fetchRatesFromCurrency when repository throws exception`() {
        runBlocking {
            given(exchangeRatesRepository.getRatesByCurrency("EUR")).willThrow(
                GetRatesByCurrencyException(
                    "exception"
                )
            )
            useCase.invoke("EUR")
        }
    }
}
