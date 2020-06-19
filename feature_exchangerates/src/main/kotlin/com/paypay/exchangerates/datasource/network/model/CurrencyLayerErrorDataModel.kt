package com.paypay.exchangerates.datasource.network.model

data class CurrencyLayerErrorDataModel(
    val code: Int,
    val info: String,
    val type: String? = null
)