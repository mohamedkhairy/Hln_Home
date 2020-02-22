package com.example.hlnhome.di


import android.app.Application
import com.example.hlnhome.BuildConfig
import com.example.hlnhome.R
import com.example.hlnhome.database.HalanDatabase
import com.example.hlnhome.database.dao.Dao
import com.example.hlnhome.factory.LiveDataCallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class AppModule {

    //////////////// database provides ///////////////

    @Singleton
    @Provides
    internal fun databseInstance(app: Application): HalanDatabase =
        HalanDatabase.getInstance(app)

    @Singleton
    @Provides
    internal fun databaseDaoProvides(halanDatabase: HalanDatabase): Dao =
        halanDatabase.HalanHomeDao()


    ////////////// Retrofit provides ////////////////

    @Singleton
    @Provides
    internal fun bearerTokenInterceptor(app: Application): Interceptor =
        Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.Token)
                    .addHeader("device", app.getString(R.string.device))
                    .addHeader("version", "android")
                    .build()
            )
        }

    fun loggerInterceptor(): Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Singleton
    @Provides
    internal fun okHttpClientProvides(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(interceptor)
            .addInterceptor(loggerInterceptor())
            .build()


    @Singleton
    @Provides
    internal fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}
