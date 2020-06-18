package com.paypay.exchangerates.framework.network.model

data class GetExchangeRatesResponseDataModel(
    val success: Boolean,
    val error: CurrencyLayerErrorDataModel? = null,
    val source: String? = null,
    val quotes: Map<String, Double>? = null
)