package com.example.hlnhome.di.modules


import com.example.hlnhome.home.HomeFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentModule {


    @ContributesAndroidInjector
    internal abstract fun contributesHomeFragment(): HomeFragment
}
