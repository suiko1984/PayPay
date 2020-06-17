package com.paypay.exchangerates.domain.entity

data class Currency(
    val code: CurrencyCode,
    val label: String
)

typealias CurrencyCode = String