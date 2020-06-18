package com.paypay.exchangerates.data.disk.sharedpreferences

import com.paypay.exchangerates.framework.SharedPreferences
import com.paypay.exchangerates.framework.SharedPreferencesNotFoundException

interface SharedPreferencesDataSource {
    fun writeLong(
        pairValues: Pair<String, Long>,
        prefsName: String = SharedPreferences.PREFS_NAME
    )

    @Throws(SharedPreferencesNotFoundException::class)
    fun readLong(
        key: String,
        prefsName: String = SharedPreferences.PREFS_NAME,
        defaultValue: Long = 0
    ): Long
}