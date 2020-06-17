package com.paypay.exchangerates.presentation.viewdata

import com.paypay.exchangerates.domain.entity.CurrencyCode

data class CurrencyViewData(
    val description: String,
    val code: CurrencyCode
)