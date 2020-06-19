package com.paypay.exchangerates.di

import com.paypay.common.datasource.room.AppDatabase
import com.paypay.common.datasource.room.CurrencyDao
import com.paypay.common.datasource.room.RateDao
import com.paypay.common.domain.usecase.UseCase
import com.paypay.exchangerates.data.ExchangeRatesRepositoryImpl
import com.paypay.exchangerates.data.disk.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.data.disk.ExchangeRatesRefreshRulesDiskDataSource
import com.paypay.exchangerates.data.disk.ExchangeRatesRefreshRulesSharedPreferences
import com.paypay.exchangerates.data.network.ExchangeRatesNetworkDataSource
import com.paypay.exchangerates.datasource.disk.ExchangeRatesRoom
import com.paypay.exchangerates.datasource.network.ExchangeRatesCurrencyLayerService
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ExchangeRatesModule {

    @ExchangeRatesScope
    @Binds
    abstract fun bindFetchCurrenciesUseCase(useCase: FetchCurrenciesUseCase): UseCase<Unit, List<Currency>>

    @ExchangeRatesScope
    @Binds
    abstract fun bindFetchRatesFromCurrencyUseCase(useCase: FetchRatesFromCurrencyUseCase): UseCase<CurrencyCode, List<ExchangeRate>>

    @ExchangeRatesScope
    @Binds
    abstract fun bindExchangeRatesRepositoryImpl(repository: ExchangeRatesRepositoryImpl): ExchangeRatesRepository

    @ExchangeRatesScope
    @Binds
    abstract fun bindExchangeRatesPersistentRulesSharedPreferences(exchangeRatesLocalPersistentRules: ExchangeRatesRefreshRulesSharedPreferences): ExchangeRatesRefreshRulesDiskDataSource

    @ExchangeRatesScope
    @Binds
    abstract fun bindExchangeRatesCurrencyLayerService(exchangeRatesCurrencyLayerService: ExchangeRatesCurrencyLayerService): ExchangeRatesNetworkDataSource

    @ExchangeRatesScope
    @Binds
    abstract fun bindCurrencyRoomDataSource(currencyRoom: ExchangeRatesRoom): ExchangeRatesDiskDataSource

    companion object {
        @Provides
        fun providesCurrencyDao(
            appDatabase: AppDatabase
        ): CurrencyDao = appDatabase.currencyDao()

        @Provides
        fun providesRateDao(
            appDatabase: AppDatabase
        ): RateDao = appDatabase.rateDao()
    }
}