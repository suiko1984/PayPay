package com.paypay.exchangerates.presentation.viewdata

import com.paypay.exchangerates.domain.entity.CurrencyCode

data class ExchangeRateViewData(
    val currencyCode: CurrencyCode,
    val unitConversion: String,
    val amountConversion: String

)