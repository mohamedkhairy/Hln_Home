package com.example.hlnhome.di


import com.example.hlnhome.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector()
    internal abstract fun contributesMainActivity(): MainActivity
}