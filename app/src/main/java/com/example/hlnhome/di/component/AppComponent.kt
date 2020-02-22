package com.example.hlnhome.di.component

import android.app.Application
import com.example.hlnhome.di.ActivityBuilderModule
import com.example.hlnhome.BaseApplication
import com.example.hlnhome.di.AppModule
import com.example.hlnhome.di.modules.EndPointModule
import com.example.hlnhome.di.modules.HomeFragmentModule
import com.example.hlnhome.di.modules.HomeViewModelModule
import com.example.hlnhome.di.modules.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        EndPointModule::class,
        HomeViewModelModule::class,
        HomeFragmentModule::class
    ]
)

interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}