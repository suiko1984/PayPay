package com.paypay.data.sharedpreferences

import com.paypay.framework.SharedPreferences
import com.paypay.framework.SharedPreferencesNotFoundException

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