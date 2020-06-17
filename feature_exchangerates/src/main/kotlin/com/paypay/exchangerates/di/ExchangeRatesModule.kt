package com.paypay.exchangerates.di

import android.content.Context
import androidx.room.Room
import com.paypay.exchangerates.data.disk.room.CurrencyDao
import com.paypay.exchangerates.data.disk.room.RateDao
import com.paypay.exchangerates.data.*
import com.paypay.exchangerates.data.disk.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.data.disk.ExchangeRatesRefreshRulesDiskDataSource
import com.paypay.exchangerates.data.network.ExchangeRatesCurrencyLayerService
import com.paypay.exchangerates.data.disk.room.AppDatabase
import com.paypay.exchangerates.data.disk.room.ExchangeRatesRoom
import com.paypay.exchangerates.data.network.ExchangeRatesNetworkDataSource
import com.paypay.exchangerates.data.disk.sharedpreferences.ExchangeRatesRefreshRulesSharedPreferences
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import com.paypay.exchangerates.domain.usecase.UseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

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
        @ExchangeRatesScope
        fun providesAppDatabase(
            @Named("context")
            context: Context
        ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "name"
        ).build()

        @Provides
        @ExchangeRatesScope
        fun providesCurrencyDao(
            appDatabase: AppDatabase
        ): CurrencyDao = appDatabase.currencyDao()

        @Provides
        @ExchangeRatesScope
        fun providesRateDao(
            appDatabase: AppDatabase
        ): RateDao = appDatabase.rateDao()
    }
}