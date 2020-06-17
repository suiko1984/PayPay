package com.paypay.di

import com.paypay.presentation.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}