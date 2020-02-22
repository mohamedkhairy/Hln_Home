package com.example.hlnhome.resource

import kotlinx.coroutines.Job

class Resource<T>(val status: AuthStatus, val data: T?, val message: String?, val job: MutableList<Job>? , val isConnected: Boolean) {


    enum class AuthStatus {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(data: T?, job: MutableList<Job>? ,isConnected: Boolean): Resource<T> {
            return Resource(AuthStatus.SUCCESS, data, null, job , isConnected)
        }

        fun <T> error(msg: String, data: T?, job: MutableList<Job>?, isConnected: Boolean): Resource<T> {
            return Resource(AuthStatus.ERROR, data, msg, job, isConnected)
        }

        fun <T> loading(data: T?, isConnected: Boolean): Resource<T> {
            return Resource(AuthStatus.LOADING, data, null, null ,isConnected)
        }
    }

}
