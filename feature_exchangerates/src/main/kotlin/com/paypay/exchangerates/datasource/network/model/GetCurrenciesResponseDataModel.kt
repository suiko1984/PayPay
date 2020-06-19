package com.paypay.exchangerates.datasource.network.model

data class GetCurrenciesResponseDataModel(
    val success: Boolean,
    val error: CurrencyLayerErrorDataModel? = null,
    val currencies: Map<String, String>? = null
)