package com.example.hlnhome.di.modules


import androidx.lifecycle.ViewModel
import com.example.hlnhome.di.key.ViewModelKey
import com.example.hlnhome.home.HomeViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel


}
