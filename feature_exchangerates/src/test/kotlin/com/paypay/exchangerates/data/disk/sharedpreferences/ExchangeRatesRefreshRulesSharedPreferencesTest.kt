package com.paypay.exchangerates.data.disk.sharedpreferences

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.only
import com.nhaarman.mockitokotlin2.then
import com.paypay.exchangerates.data.date.DateProvider
import com.paypay.exchangerates.framework.SharedPreferencesNotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeRatesRefreshRulesSharedPreferencesTest {

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var sharedPreferences: SharedPreferencesDataSource

    @InjectMocks
    lateinit var rules: ExchangeRatesRefreshRulesSharedPreferences

    @Test
    fun `shouldRefreshData when frequency has reached, should return true`() = runBlocking {
        given(dateProvider.currentDate).willReturn(Date(10000000L))
        given(
            sharedPreferences.readLong(
                "LATEST_REFRESH_DATE",
                "EXCHANGE_RATES_PREFS_NAME"
            )
        ).willReturn(8000000L)
        assert(rules.shouldUpdate())
    }

    @Test
    fun `shouldRefreshData when user has never written in sharedPreferences, should return true`() =
        runBlocking {
            given(dateProvider.currentDate).willReturn(Date(10000000L))
            given(
                sharedPreferences.readLong(
                    "LATEST_REFRESH_DATE",
                    "EXCHANGE_RATES_PREFS_NAME"
                )
            ).willReturn(0L)
            assert(rules.shouldUpdate())
        }

    @Test
    fun `shouldRefreshData when sharedPreferences throws SharedPreferencesNotFoundException, should return true`() =
        runBlocking {
            given(dateProvider.currentDate).willReturn(Date(10000000L))
            given(
                sharedPreferences.readLong(
                    "LATEST_REFRESH_DATE",
                    "EXCHANGE_RATES_PREFS_NAME"
                )
            ).willThrow(SharedPreferencesNotFoundException())
            assert(rules.shouldUpdate())
        }

    @Test
    fun `updateRefreshDate when normal case`() = runBlocking {
        given(dateProvider.currentDate).willReturn(Date(1000))
        rules.update()
        then(sharedPreferences).should(only())
            .writeLong("LATEST_REFRESH_DATE" to 1000, "EXCHANGE_RATES_PREFS_NAME")
    }
}