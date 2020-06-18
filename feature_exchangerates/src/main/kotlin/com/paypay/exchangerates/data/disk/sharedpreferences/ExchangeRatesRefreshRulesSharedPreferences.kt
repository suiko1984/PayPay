package com.paypay.exchangerates.data.disk.sharedpreferences

import com.paypay.exchangerates.data.date.DateProvider
import javax.inject.Inject

class ExchangeRatesRefreshRulesSharedPreferences @Inject constructor(
    private val dateProvider: DateProvider,
    private val sharedPreferences: SharedPreferencesDataSource
) : ExchangeRatesRefreshRulesDiskDataSource {

    override suspend fun shouldUpdate(): Boolean = runCatching {
        val now = dateProvider.currentDate.time
        val lastRefreshTime = sharedPreferences.readLong(
            LATEST_REFRESH_DATE,
            EXCHANGE_RATES_PREFS_NAME
        )
        lastRefreshTime == 0L || now - lastRefreshTime > REFRESH_FREQUENCY_IN_MS
    }.onFailure {
        return true
    }.getOrThrow()

    override suspend fun update() {
        sharedPreferences.writeLong(
            LATEST_REFRESH_DATE to dateProvider.currentDate.time,
            EXCHANGE_RATES_PREFS_NAME
        )
    }

    companion object {
        private const val LATEST_REFRESH_DATE = "LATEST_REFRESH_DATE"
        private const val EXCHANGE_RATES_PREFS_NAME = "EXCHANGE_RATES_PREFS_NAME"
        private const val REFRESH_FREQUENCY_IN_MS = 1000L * 60L * 30L // 30 minutes
    }
}