package com.paypay.exchangerates.di

import android.content.Context
import com.paypay.di.AppComponent
import com.paypay.exchangerates.presentation.view.ExchangeRatesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

@Suppress("unused")
@ExchangeRatesScope
@Component(
    modules = [ExchangeRatesViewModelModule::class, ExchangeRatesModule::class, ExchangeRatesServiceModule::class],
    dependencies = [AppComponent::class]
)
interface ExchangeRatesComponent {

    fun inject(fragment: ExchangeRatesFragment)

    @Component.Factory
    interface Factory {

        fun create(
            appComponent: AppComponent,
            @BindsInstance
            @Named("context")
            context: Context
        ): ExchangeRatesComponent
    }
}