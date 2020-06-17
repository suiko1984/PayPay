package com.paypay.exchangerates.data.network.model

data class CurrencyLayerErrorDataModel(
    val code: Int,
    val info: String,
    val type: String? = null
)