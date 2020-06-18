package com.paypay.exchangerates.framework.sharedpreferences

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class SharedPreferencesTest {

    @Mock
    lateinit var context: Context

    @InjectMocks
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var androidSharedPreferences: android.content.SharedPreferences

    @Mock
    var sharedPreferencesEditor: android.content.SharedPreferences.Editor? = null

    @Test
    fun writeLong() {
        val pairWithLong = "longKey" to 2L
        given(context.getSharedPreferences("paypay_preferences", Context.MODE_PRIVATE)).willReturn(
            androidSharedPreferences
        )
        given(androidSharedPreferences.edit()).willReturn((sharedPreferencesEditor))

        sharedPreferences.writeLong(pairWithLong, "paypay_preferences")

        then(sharedPreferencesEditor).should()?.putLong("longKey", 2L)
        then(sharedPreferencesEditor).should()?.apply()
        verifyNoMoreInteractions(sharedPreferencesEditor)
    }

    @Test
    fun `readLong when normal case`() {
        given(context.getSharedPreferences("paypay_preferences", Context.MODE_PRIVATE)).willReturn(
            androidSharedPreferences
        )
        given(androidSharedPreferences.getLong("longKey", 0L)).willReturn(2L)
        val result = sharedPreferences.readLong("longKey", "paypay_preferences")

        assert(result == 2L)
    }

    @Test(expected = SharedPreferencesNotFoundException::class)
    fun `readLong when SharedPreferencesNotFoundException`() {
        given(context.getSharedPreferences("paypay_preferences", Context.MODE_PRIVATE)).willReturn(null)
        sharedPreferences.readLong("longKey", "paypay_preferences")
    }
}