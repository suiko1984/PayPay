package com.paypay.exchangerates.di

import com.paypay.exchangerates.framework.network.CurrencyLayerService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ExchangeRatesServiceModule {

    @Provides
    fun providesCurrencyLayerService(): CurrencyLayerService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyLayerService::class.java)
    }

    companion object {
        const val BASE_URL = "http://api.currencylayer.com"
        const val GET_CURRENCIES_PATH = "/list"
        const val GET_RATES_PATH = "/live"
        const val SOURCE_KEY_PARAM_NAME = "source"
        const val ACCESS_KEY_PARAM_NAME = "access_key"
        const val ACCESS_KEY_PARAM_VALUE = "96c6c8ab84215cb3986cec89858fabe0"
    }
}