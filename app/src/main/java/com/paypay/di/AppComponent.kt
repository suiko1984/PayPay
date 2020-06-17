package com.paypay.di

import android.content.Context
import com.paypay.PayPayApp
import com.paypay.data.sharedpreferences.SharedPreferencesDataSource
import com.paypay.data.date.DateProvider
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Suppress("unused")
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    fun sharedPreferencesDataSource(): SharedPreferencesDataSource
    fun dateProvider(): DateProvider
    fun inject(tagtagApp: PayPayApp)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            @Named("context")
            context: Context,
            @BindsInstance
            module: AppModule
        ): AppComponent
    }
}