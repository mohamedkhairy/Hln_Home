package com.example.hlnhome.network


import java.io.IOException

import retrofit2.Response


open class ApiResponse<T> {

    fun create(error: Throwable): ApiResponse<T> {
        return ApiErrorResponse(if (!error.message.equals("")) error.message else "Unknown error\nCheck network connection")
    }

    fun create(response: Response<T>): ApiResponse<T> {

        if (response.isSuccessful) {

            val body = response.body()

            return if (body == null || response.code() == 204) { // 204 is empty response

                ApiEmptyResponse()
            } else {

                ApiSuccessResponse(body)
            }
        } else {

            var errorMsg = "No Response"
            try {
                errorMsg = response.errorBody()!!.string()
            } catch (e: IOException) {
                e.printStackTrace()
                errorMsg = response.message()
            }

            return ApiErrorResponse(errorMsg)
        }
    }

    /**
     * Generic success response from api
     *
     * @param <T>
    </T> */
    data class ApiSuccessResponse<T> internal constructor(val body: T) : ApiResponse<T>()

    /**
     * Generic Error response from API
     *
     * @param <T>
    </T> */
    data class ApiErrorResponse<T> internal constructor(val errorMessage: String?) :
        ApiResponse<T>()

    /**
     * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
     */
    class ApiEmptyResponse<T> : ApiResponse<T>()

}





















