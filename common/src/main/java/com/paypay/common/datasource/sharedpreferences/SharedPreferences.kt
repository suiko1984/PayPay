package com.paypay.common.datasource.sharedpreferences

import android.content.Context
import androidx.core.content.edit
import com.paypay.common.data.disk.sharedpreferences.SharedPreferencesDataSource
import com.paypay.common.data.disk.sharedpreferences.SharedPreferencesNotFoundException
import javax.inject.Inject

class SharedPreferences @Inject constructor(private val context: Context) :
    SharedPreferencesDataSource {

    companion object {
        const val PREFS_NAME = "paypay_preferences"
    }

    override fun writeLong(pairValues: Pair<String, Long>, prefsName: String) {
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)?.edit {
            putLong(pairValues.first, pairValues.second)
        }
    }

    override fun readLong(key: String, prefsName: String, defaultValue: Long): Long {
        val sharedPref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            ?: throw SharedPreferencesNotFoundException()
        return sharedPref.getLong(key, defaultValue)
    }
}