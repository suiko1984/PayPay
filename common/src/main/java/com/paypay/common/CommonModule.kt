package com.paypay.common

import android.content.Context
import androidx.room.Room
import com.paypay.common.data.inmemory.DateProvider
import com.paypay.common.datasource.room.AppDatabase
import com.paypay.common.datasource.sharedpreferences.SharedPreferences
import com.paypay.common.data.disk.sharedpreferences.SharedPreferencesDataSource
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class CommonModule {

    companion object {
        @Provides
        @Singleton
        fun providesAppDatabase(
            @Named("context")
            context: Context
        ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "name"
        ).build()

        @Provides
        @Singleton
        fun providesSharedPreferencesDataSource(
            @Named("context")
            context: Context
        ): SharedPreferencesDataSource =
            SharedPreferences(
                context
            )

        @Provides
        fun providesDateProvider(): DateProvider = object :
            DateProvider {
            override val currentDate: Date
                get() = Calendar.getInstance().time
        }
    }
}