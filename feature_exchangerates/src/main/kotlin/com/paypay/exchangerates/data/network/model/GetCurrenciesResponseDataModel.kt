package com.paypay.exchangerates.data.network.model

data class GetCurrenciesResponseDataModel(
    val success: Boolean,
    val error: CurrencyLayerErrorDataModel? = null,
    val currencies: Map<String, String>? = null
)