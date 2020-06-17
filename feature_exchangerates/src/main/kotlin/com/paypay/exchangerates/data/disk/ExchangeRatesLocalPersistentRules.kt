package com.paypay.exchangerates.data.disk

interface ExchangeRatesRefreshRulesDiskDataSource {
    suspend fun shouldUpdate(): Boolean
    suspend fun update()
}