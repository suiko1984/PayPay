package com.paypay.exchangerates.data.disk.sharedpreferences

interface ExchangeRatesRefreshRulesDiskDataSource {
    suspend fun shouldUpdate(): Boolean
    suspend fun update()
}