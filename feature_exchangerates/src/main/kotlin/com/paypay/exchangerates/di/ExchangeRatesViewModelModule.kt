package com.paypay.exchangerates.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paypay.exchangerates.presentation.viewmodel.ExchangeRatesViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class ExchangeRatesViewModelModule {

    @ExchangeRatesScope
    @Binds
    abstract fun bindsViewModelFactory(factory: ExchangeRatesViewModelFactory): ViewModelProvider.Factory

    @ExchangeRatesScope
    @Binds
    abstract fun bindExchangeRatesViewModel(viewModel: ExchangeRatesViewModel): ViewModel
}