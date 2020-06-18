package com.paypay.di

import com.paypay.PayPayApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Suppress("unused")
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent {

    fun inject(tagtagApp: PayPayApp)

    @Component.Factory
    interface Factory {

        fun create(): AppComponent
    }
}