package com.paypay.common.data.disk.sharedpreferences

import com.paypay.common.datasource.sharedpreferences.SharedPreferences

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

class SharedPreferencesNotFoundException : Exception()