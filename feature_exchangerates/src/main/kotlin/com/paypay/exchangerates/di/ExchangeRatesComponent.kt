package com.paypay.exchangerates.di

import android.content.Context
import com.paypay.common.CommonComponent
import com.paypay.common.datasource.room.CurrencyDao
import com.paypay.common.datasource.room.RateDao
import com.paypay.di.AppComponent
import com.paypay.exchangerates.presentation.view.ExchangeRatesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

@Suppress("unused")
@ExchangeRatesScope
@Component(
    modules = [ExchangeRatesViewModelModule::class, ExchangeRatesModule::class, ExchangeRatesServiceModule::class],
    dependencies = [AppComponent::class, CommonComponent::class]
)
interface ExchangeRatesComponent {

    fun currencyDao(): CurrencyDao
    fun rateDao(): RateDao
    fun inject(fragment: ExchangeRatesFragment)

    @Component.Factory
    interface Factory {

        fun create(
            appComponent: AppComponent,
            commonComponent: CommonComponent,
            @BindsInstance
            @Named("context")
            context: Context
        ): ExchangeRatesComponent
    }
}