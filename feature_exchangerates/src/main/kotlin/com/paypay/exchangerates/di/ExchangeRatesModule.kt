package com.paypay.exchangerates.di

import android.content.Context
import androidx.room.Room
import com.paypay.exchangerates.data.ExchangeRatesRepositoryImpl
import com.paypay.exchangerates.data.date.DateProvider
import com.paypay.exchangerates.data.disk.room.ExchangeRatesDiskDataSource
import com.paypay.exchangerates.data.disk.sharedpreferences.ExchangeRatesRefreshRulesDiskDataSource
import com.paypay.exchangerates.framework.room.AppDatabase
import com.paypay.exchangerates.framework.room.CurrencyDao
import com.paypay.exchangerates.framework.room.ExchangeRatesRoom
import com.paypay.exchangerates.framework.room.RateDao
import com.paypay.exchangerates.data.disk.sharedpreferences.ExchangeRatesRefreshRulesSharedPreferences
import com.paypay.exchangerates.data.disk.sharedpreferences.SharedPreferencesDataSource
import com.paypay.exchangerates.framework.network.ExchangeRatesCurrencyLayerService
import com.paypay.exchangerates.data.network.ExchangeRatesNetworkDataSource
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import com.paypay.exchangerates.domain.usecase.FetchCurrenciesUseCase
import com.paypay.exchangerates.domain.usecase.FetchRatesFromCurrencyUseCase
import com.paypay.exchangerates.domain.usecase.UseCase
import com.paypay.exchangerates.framework.sharedpreferences.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.util.*
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

        @Provides
        fun providesDateProvider() = object : DateProvider {
            override val currentDate: Date
                get() = Calendar.getInstance().time
        }

        @Provides
        @ExchangeRatesScope
        fun providesSharedPreferencesDataSource(
            @Named("context")
            context: Context
        ): SharedPreferencesDataSource =
            SharedPreferences(
                context
            )
    }
}