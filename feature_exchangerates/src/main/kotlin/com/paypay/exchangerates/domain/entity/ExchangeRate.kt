package com.paypay.exchangerates.domain.entity

data class ExchangeRate(
    val currencyCode: CurrencyCode,
    val rate: Double
)