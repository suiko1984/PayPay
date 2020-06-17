package com.paypay.exchangerates.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesException
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyException
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import com.paypay.exchangerates.presentation.extension.getOrAwaitValue
import com.paypay.exchangerates.presentation.extension.setValue
import com.paypay.exchangerates.presentation.util.CoroutinesTestRule
import com.paypay.exchangerates.presentation.viewdata.CurrencyViewData
import com.paypay.exchangerates.presentation.viewdata.ExchangeRateViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExchangeRatesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var fetchCurrenciesUseCase: FetchCurrenciesUseCase

    @Mock
    private lateinit var fetchRatesFromCurrencyUseCase: FetchRatesFromCurrencyUseCase

    @Mock
    private lateinit var amountObserver: Observer<Double>

    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var hasErrorObserver: Observer<Boolean>

    @Mock
    private lateinit var errorObserver: Observer<String>

    @Mock
    private lateinit var currenciesObserver: Observer<List<CurrencyViewData>>

    @Mock
    private lateinit var selectedCurrencyObserver: Observer<CurrencyViewData>

    @Mock
    private lateinit var exchangeRatesObserver: Observer<List<ExchangeRateViewData>>

    @Mock
    private lateinit var ratesIsLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var ratesHasErrorObserver: Observer<Boolean>

    @Mock
    private lateinit var ratesErrorObserver: Observer<String>

    @InjectMocks
    private lateinit var viewModel: ExchangeRatesViewModel

    @Before
    fun setup() {
        viewModel.amount.observeForever(amountObserver)
        viewModel.isLoading.observeForever(isLoadingObserver)
        viewModel.hasError.observeForever(hasErrorObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.currencies.observeForever(currenciesObserver)
        viewModel.selectedCurrency.observeForever(selectedCurrencyObserver)
        viewModel.exchangeRates.observeForever(exchangeRatesObserver)
        viewModel.ratesIsLoading.observeForever(ratesIsLoadingObserver)
        viewModel.ratesHasError.observeForever(ratesHasErrorObserver)
        viewModel.ratesError.observeForever(ratesErrorObserver)
    }

    @Test
    fun `fetchCurrencies when normal case`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        given(fetchCurrenciesUseCase.invoke(Unit)).willReturn(
            listOf(
                Currency(
                    "EUR",
                    "Euro"
                ), Currency(
                    "USD",
                    "US Dollar"
                ), Currency(
                    "JPY",
                    "Japanese Yen"
                ), Currency(
                    "GBP",
                    "British Pound"
                )
            )
        )
        viewModel.fetchCurrencies()
        verify(currenciesObserver, times(1)).onChanged(emptyList())
        verify(isLoadingObserver, times(1)).onChanged(true)
        verify(currenciesObserver, times(1)).onChanged(
            listOf(
                CurrencyViewData(
                    "EUR Euro",
                    "EUR"
                ),
                CurrencyViewData(
                    "USD US Dollar",
                    "USD"
                ),
                CurrencyViewData(
                    "JPY Japanese Yen",
                    "JPY"
                ),
                CurrencyViewData(
                    "GBP British Pound",
                    "GBP"
                )
            )
        )
        assertEquals(viewModel.isLoading.getOrAwaitValue(), false)
    }

    @Test
    fun `fetchCurrencies when exception`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        given(fetchCurrenciesUseCase.invoke(Unit)).willThrow(FetchCurrenciesException("Exception"))
        viewModel.fetchCurrencies()
        verify(isLoadingObserver, times(1)).onChanged(true)
        assertEquals(viewModel.hasError.getOrAwaitValue(), true)
        verify(errorObserver, times(1)).onChanged("Exception")
        assertEquals(viewModel.isLoading.getOrAwaitValue(), false)
    }

    @Test
    fun `selectAmount with value`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.selectAmount(20.0)
        verify(amountObserver, times(1)).onChanged(20.0)
    }

    @Test
    fun `selectAmount with null value`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.amount.setValue(30.0)
        viewModel.selectAmount(null)
        verify(amountObserver, times(2)).onChanged(30.0)
    }

    @Test
    fun selectCurrency() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val currency = CurrencyViewData(
            description = "description",
            code = "EUR"
        )
        viewModel.selectCurrency(currency)
        verify(selectedCurrencyObserver, times(1)).onChanged(currency)
    }

    @Test
    fun `fetchExchangeRates when normal case`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.selectedCurrency.setValue(
                CurrencyViewData(
                    "EUR Euro",
                    "EUR"
                )
            )
            viewModel.amount.setValue(2.0)
            val input = "EUR"
            given(fetchRatesFromCurrencyUseCase.invoke(input)).willReturn(
                listOf(
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
            )
            viewModel.fetchExchangeRates()
            verify(ratesHasErrorObserver, times(1)).onChanged(false)
            verify(exchangeRatesObserver, times(1)).onChanged(emptyList())
            verify(ratesIsLoadingObserver, times(1)).onChanged(true)
            assertEquals(
                viewModel.exchangeRates.getOrAwaitValue(), listOf(
                    ExchangeRateViewData(
                        currencyCode = "EUR",
                        unitConversion = "1.0 EUR = 1.0 EUR",
                        amountConversion = "2.0 EUR = 2.0 EUR"
                    ),
                    ExchangeRateViewData(
                        currencyCode = "USD",
                        unitConversion = "1.0 EUR = 1.12412 USD",
                        amountConversion = "2.0 EUR = 2.24824 USD"
                    ),
                    ExchangeRateViewData(
                        currencyCode = "JPY",
                        unitConversion = "1.0 EUR = 120.784 JPY",
                        amountConversion = "2.0 EUR = 241.568 JPY"
                    ),
                    ExchangeRateViewData(
                        currencyCode = "GBP",
                        unitConversion = "1.0 EUR = 0.898827 GBP",
                        amountConversion = "2.0 EUR = 1.797654 GBP"
                    )
                )
            )
            assertEquals(viewModel.ratesIsLoading.getOrAwaitValue(), false)
        }

    @Test
    fun `fetchExchangeRates when exception`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.selectedCurrency.setValue(
                CurrencyViewData(
                    "EUR Euro",
                    "EUR"
                )
            )
            viewModel.amount.setValue(2.0)
            val input = "EUR"
            given(fetchRatesFromCurrencyUseCase.invoke(input)).willThrow(
                FetchRatesFromCurrencyException("Exception")
            )
            viewModel.fetchExchangeRates()
            verify(ratesHasErrorObserver, times(1)).onChanged(false)
            verify(ratesIsLoadingObserver, times(1)).onChanged(true)
            assertEquals(viewModel.ratesHasError.getOrAwaitValue(), true)
            assertEquals(viewModel.ratesError.getOrAwaitValue(), "Exception")
            assertEquals(viewModel.ratesIsLoading.getOrAwaitValue(), false)
        }

    @Test
    fun `fetchExchangeRates when rate list is not empty, should not display loader`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.exchangeRates.setValue(
                listOf(
                    ExchangeRateViewData(
                        currencyCode = "EUR",
                        unitConversion = "1.0 EUR = 1.0 EUR",
                        amountConversion = "2.0 EUR = 2.0 EUR"
                    )
                )
            )
            viewModel.fetchExchangeRates()
            verify(ratesHasErrorObserver, times(1)).onChanged(false)
            verify(ratesIsLoadingObserver, times(0)).onChanged(true)
        }

    @Test
    fun `fetchExchangeRates when rate has error, should display loader`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.ratesHasError.setValue(true)
            viewModel.exchangeRates.setValue(
                listOf(
                    ExchangeRateViewData(
                        currencyCode = "EUR",
                        unitConversion = "1.0 EUR = 1.0 EUR",
                        amountConversion = "2.0 EUR = 2.0 EUR"
                    )
                )
            )
            viewModel.fetchExchangeRates()
            verify(ratesHasErrorObserver, times(1)).onChanged(false)
            verify(ratesIsLoadingObserver, times(1)).onChanged(true)
        }
}