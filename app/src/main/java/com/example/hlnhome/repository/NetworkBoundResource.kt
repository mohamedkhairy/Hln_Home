package com.example.hlnhome.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.hlnhome.network.ApiResponse
import com.example.hlnhome.resource.Resource
import kotlinx.coroutines.*

abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private val job: MutableList<Job> = mutableListOf()
    private val results = MediatorLiveData<Resource<CacheObject>>()

    init {

        val dbSource = this.loadFromDb()


        results.addSource(dbSource) { data ->
            results.removeSource(dbSource)
            if (shouldFetch(data)) {
                // get data from the network
                fetchFromNetwork(dbSource)
            } else {

                results.addSource(dbSource) { cacheObject1 ->
                    setValue(
                        Resource.success(
                            cacheObject1,
                            job,
                            false
                        )
                    )
                }
            }
        }

    }


    private fun fetchFromNetwork(dbSource: LiveData<CacheObject?>) {


        val apiResponse = makeApiCall()


        // update LiveData for loading status
        results.addSource(dbSource) { cacheObject ->
            setValue(Resource.loading(cacheObject, true))
        }


        results.addSource(apiResponse) { requestObjectApiResponse ->
            results.removeSource(dbSource)
            results.removeSource(apiResponse)


            /*
                    3 cases:
                       1) ApiSuccessResponse
                       2) ApiErrorResponse
                       3) ApiEmptyResponse
                 */


            when (requestObjectApiResponse) {
                is ApiResponse.ApiSuccessResponse<*> -> {

                    job += GlobalScope.launch(Dispatchers.IO) {
                        // save the response to the local db
                        saveCallResult(processResponse(requestObjectApiResponse) as RequestObject)
                        withContext(Dispatchers.Main) {
                            results.addSource<CacheObject>(loadFromDb()) { cacheObject ->
                                setValue(Resource.success(cacheObject, job, true))
                            }
                        }
                    }
                }


                is ApiResponse.ApiEmptyResponse<*> -> {
                    job += GlobalScope.launch(Dispatchers.Main) {
                        results.addSource<CacheObject>(loadFromDb()) { cacheObject ->
                            setValue(Resource.success(cacheObject, job, false))
                        }
                    }
                }


                is ApiResponse.ApiErrorResponse<*> -> {

                    results.addSource(dbSource) { cacheObject ->

                        setValue(
                            Resource.error(
                                (requestObjectApiResponse).errorMessage!!, cacheObject, job, false
                            )
                        )
                    }
                }
            }
        }
    }


    private fun processResponse(response: ApiResponse.ApiSuccessResponse<*>): CacheObject {
        return response.body as CacheObject
    }

    private fun setValue(newValue: Resource<CacheObject>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    protected abstract fun loadFromDb(): LiveData<CacheObject?>

    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    protected abstract fun makeApiCall(): LiveData<ApiResponse<RequestObject?>>

    protected abstract fun saveCallResult(data: RequestObject)

    fun asLiveData(): LiveData<Resource<CacheObject>> = results


}