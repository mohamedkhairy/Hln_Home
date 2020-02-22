package com.example.hlnhome.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hlnhome.database.dao.Dao
import com.example.hlnhome.database.entity.HalanHome
import com.example.hlnhome.database.entity.ServiceCategories
import com.example.hlnhome.network.ApiResponse
import com.example.hlnhome.network.EndPoints
import com.example.hlnhome.resource.Resource
import javax.inject.Inject

class Repository
@Inject
constructor(val dao: Dao, val endPoints: EndPoints, val application: Application) {


    fun getHalanHomeData(long: String, lat: String): LiveData<Resource<List<ServiceCategories>>> {

        return object : NetworkBoundResource<List<ServiceCategories>, HalanHome>() {

            override fun shouldFetch(data: List<ServiceCategories>?): Boolean =
                data.isNullOrEmpty()


            override fun makeApiCall(): LiveData<ApiResponse<HalanHome?>> =
                endPoints.callHalanHome(long, lat)


            override fun saveCallResult(halanHome: HalanHome) {
                dao.deleteAll()
                dao.insertAll(halanHome.data)
            }

            override fun loadFromDb(): LiveData<List<ServiceCategories>?> =
                dao.getServiceCategoriesData()

        }.asLiveData()
    }


}