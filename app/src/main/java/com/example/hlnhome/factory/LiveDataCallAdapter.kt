package com.example.hlnhome.factory


import androidx.lifecycle.LiveData
import com.example.hlnhome.network.ApiResponse
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response

class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            override fun onActive() {
                super.onActive()
                val apiResponse = ApiResponse<R>()
                if (!call.isExecuted) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(apiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            postValue(apiResponse.create(t))
                        }
                    })
                }

            }
        }
    }

}







