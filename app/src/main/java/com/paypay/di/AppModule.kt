package com.paypay.di

import android.content.Context
import com.paypay.data.date.DateProvider
import com.paypay.data.sharedpreferences.SharedPreferencesDataSource
import com.paypay.framework.SharedPreferences
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun providesDateProvider() = object : DateProvider {
        override val currentDate: Date
            get() = Calendar.getInstance().time
    }

    @Provides
    @Singleton
    fun providesSharedPreferencesDataSource(
        @Named("context")
        context: Context
    ): SharedPreferencesDataSource = SharedPreferences(context)
}