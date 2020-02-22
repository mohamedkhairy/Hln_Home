package com.example.hlnhome.di.modules

import com.example.hlnhome.network.EndPoints

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EndPointModule {

    @Provides
    fun provideApi(retrofit: Retrofit): EndPoints = retrofit.create(EndPoints::class.java)

}
