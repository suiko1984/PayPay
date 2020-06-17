package com.paypay.exchangerates.domain.usecase

import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import com.paypay.exchangerates.domain.repository.UnauthenticatedExeption
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class FetchCurrenciesUseCaseTest {

    @Mock
    lateinit var exchangeRatesRepository: ExchangeRatesRepository

    @InjectMocks
    lateinit var useCase: FetchCurrenciesUseCase

    @Test
    fun `fetchCurrencies with normal case`() = runBlocking {
        val list = listOf(
            Currency("EUR", "Euro"),
            Currency("USD", "US Dollar"),
            Currency("JPY", "Japanese Yen"),
            Currency("GBP", "British Pound")
        )
        given(exchangeRatesRepository.getCurrencies()).willReturn(list)
        val result = useCase.invoke(Unit)
        assert(result == list)
    }

    @Test(expected = FetchCurrenciesException::class)
    fun `fetchCurrencies with exception`() = runBlocking {
        given(exchangeRatesRepository.getCurrencies()).willThrow(
            UnauthenticatedExeption(
                "missing_access_key"
            )
        )
        val result = useCase.invoke(Unit)
        assert(result == emptyList<Currency>())
    }
}
