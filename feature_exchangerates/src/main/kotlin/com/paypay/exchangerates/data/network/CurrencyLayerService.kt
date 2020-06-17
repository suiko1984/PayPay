package com.paypay.exchangerates.data.network

import com.paypay.exchangerates.data.network.model.GetCurrenciesResponseDataModel
import com.paypay.exchangerates.data.network.model.GetExchangeRatesResponseDataModel
import com.paypay.exchangerates.di.ExchangeRatesServiceModule.Companion.ACCESS_KEY_PARAM_NAME
import com.paypay.exchangerates.di.ExchangeRatesServiceModule.Companion.ACCESS_KEY_PARAM_VALUE
import com.paypay.exchangerates.di.ExchangeRatesServiceModule.Companion.GET_CURRENCIES_PATH
import com.paypay.exchangerates.di.ExchangeRatesServiceModule.Companion.GET_RATES_PATH
import com.paypay.exchangerates.di.ExchangeRatesServiceModule.Companion.SOURCE_KEY_PARAM_NAME
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerService {

    @GET(GET_CURRENCIES_PATH)
    fun getCurrencies(
        @Query(ACCESS_KEY_PARAM_NAME)
        accessToken: String = ACCESS_KEY_PARAM_VALUE
    ): Call<GetCurrenciesResponseDataModel>

    @GET(GET_RATES_PATH)
    fun getExchangeRatesByCurrency(
        @Query(SOURCE_KEY_PARAM_NAME)
        source: String,
        @Query(ACCESS_KEY_PARAM_NAME)
        accessToken: String = ACCESS_KEY_PARAM_VALUE
    ): Call<GetExchangeRatesResponseDataModel>
}