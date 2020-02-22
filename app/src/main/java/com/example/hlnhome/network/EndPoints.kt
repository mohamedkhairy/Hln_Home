package com.example.hlnhome.network

import androidx.lifecycle.LiveData
import com.example.hlnhome.database.entity.HalanHome
import retrofit2.http.GET
import retrofit2.http.Header

interface EndPoints {

    @GET("user/services")
    fun callHalanHome (@Header("long") long: String, @Header("lat") lat: String): LiveData<ApiResponse<HalanHome?>>


}