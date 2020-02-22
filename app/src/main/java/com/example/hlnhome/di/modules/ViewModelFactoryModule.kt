package com.example.hlnhome.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.hlnhome.factory.ViewModelProviderFactory

import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

}
