package com.paypay.exchangerates.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import com.paypay.common.presentation.extension.setValue
import com.paypay.common.presentation.extension.value
import com.paypay.exchangerates.presentation.viewdata.CurrencyViewData
import com.paypay.exchangerates.presentation.viewdata.ExchangeRateViewData
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeRatesViewModel @Inject constructor(
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase,
    private val fetchRatesFromCurrencyUseCase: FetchRatesFromCurrencyUseCase
) : ViewModel() {

    val amount: LiveData<Double> by lazy {
        MutableLiveData<Double>(0.0)
    }
    val isLoading: LiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val hasError: LiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val error: LiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currencies: LiveData<List<CurrencyViewData>> by lazy {
        MutableLiveData<List<CurrencyViewData>>(emptyList())
    }.also {
        fetchCurrencies()
    }
    val selectedCurrency: LiveData<CurrencyViewData> by lazy {
        MutableLiveData<CurrencyViewData>()
    }
    val exchangeRates: LiveData<List<ExchangeRateViewData>> by lazy {
        MutableLiveData<List<ExchangeRateViewData>>(emptyList())
    }
    val ratesIsLoading: LiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val ratesHasError: LiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val ratesError: LiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun fetchCurrencies() = viewModelScope.launch {
        isLoading.setValue(true)
        runCatching {
            val currencyList = fetchCurrenciesUseCase.invoke(Unit).map {
                CurrencyViewData(description = "${it.code} ${it.label}", code = it.code)
            }
            currencies.setValue(currencyList)
        }.exceptionOrNull()?.let {
            hasError.setValue(true)
            error.setValue(it.message)
        }
        isLoading.setValue(false)
    }

    fun selectAmount(newAmount: Double?) = viewModelScope.launch {
        newAmount?.let { amount.setValue(it) } ?: amount.setValue(amount.value)
        fetchExchangeRates()
    }

    fun selectCurrency(currency: CurrencyViewData) = viewModelScope.launch {
        selectedCurrency.setValue(currency)
        fetchExchangeRates()
    }

    fun fetchExchangeRates() = viewModelScope.launch {
        if (exchangeRates.value?.isEmpty() == true || ratesHasError.value == true) ratesIsLoading.setValue(true)
        ratesHasError.setValue(false)
        runCatching {
            val exchangeRateList = fetchRatesFromCurrencyUseCase.invoke(selectedCurrency.value().code)
                .map {
                    ExchangeRateViewData(
                        currencyCode = it.currencyCode,
                        unitConversion = "1.0 ${selectedCurrency.value?.code} = ${it.rate} ${it.currencyCode}",
                        amountConversion = "${amount.value} ${selectedCurrency.value?.code} = ${it.rate * amount.value()} ${it.currencyCode}"
                    )
                }
            exchangeRates.setValue(exchangeRateList)
        }.exceptionOrNull()?.let {
            ratesHasError.setValue(true)
            ratesError.setValue(it.message)
        }
        ratesIsLoading.setValue(false)
    }
}