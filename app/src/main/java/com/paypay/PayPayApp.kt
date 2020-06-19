package com.paypay

import android.app.Application
import com.paypay.common.CommonComponent
import com.paypay.common.DaggerCommonComponent
import com.paypay.di.AppComponent
import com.paypay.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PayPayApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var appComponent: AppComponent
    lateinit var commonComponent: CommonComponent

    override fun onCreate() {
        super.onCreate()
        commonComponent = DaggerCommonComponent.factory().create(applicationContext)
        appComponent = DaggerAppComponent
            .factory()
            .create(commonComponent)
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}