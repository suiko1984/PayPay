package com.paypay.exchangerates.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import com.paypay.exchangerates.presentation.viewmodel.ExchangeRatesViewModel
import javax.inject.Inject

class ExchangeRatesViewModelFactory @Inject constructor(
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase,
    private val fetchRatesFromCurrencyUseCase: FetchRatesFromCurrencyUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeRatesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeRatesViewModel(
                fetchCurrenciesUseCase,
                fetchRatesFromCurrencyUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}