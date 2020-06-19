package com.paypay.di

import com.paypay.PayPayApp
import com.paypay.common.CommonComponent
import dagger.Component
import dagger.android.AndroidInjectionModule

@AppScope
@Suppress("unused")
@Component(
    dependencies = [CommonComponent::class],
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent {

    fun inject(tagtagApp: PayPayApp)

    @Component.Factory
    interface Factory {

        fun create(commonComponent: CommonComponent): AppComponent
    }
}