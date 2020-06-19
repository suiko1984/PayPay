package com.paypay.common

import android.content.Context
import com.paypay.common.data.inmemory.DateProvider
import com.paypay.common.datasource.room.AppDatabase
import com.paypay.common.data.disk.sharedpreferences.SharedPreferencesDataSource
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Suppress("unused")
@Component(
    modules = [CommonModule::class]
)
interface CommonComponent {

    fun appDatabase(): AppDatabase
    fun dateProvider(): DateProvider
    fun sharedPreferencesDataSource(): SharedPreferencesDataSource

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            @Named("context")
            context: Context
        ): CommonComponent
    }
}