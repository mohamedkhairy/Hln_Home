package com.example.hlnhome.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.hlnhome.database.entity.ServiceCategories
import com.example.hlnhome.repository.Repository
import com.example.hlnhome.resource.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HomeViewModel
@Inject
constructor(private val userRepository: Repository, val app: Application) : AndroidViewModel(app) {

    private var jobs: MutableList<Job>? = mutableListOf()

    private val liveHomeData: MediatorLiveData<List<ServiceCategories>> = MediatorLiveData()

    private val liveLoading: MutableLiveData<Boolean> = MutableLiveData()


    fun getLoading(): LiveData<Boolean> = liveLoading

    fun getHomeData(): LiveData<List<ServiceCategories>> = liveHomeData


    fun getHalanHomeData(long: String, lat: String) {


        val teamData = userRepository.getHalanHomeData(long, lat)


        liveHomeData.addSource(teamData) { resource ->

            when (resource?.status) {


                Resource.AuthStatus.SUCCESS -> {
                    liveHomeData.value = resource.data
                    liveLoading.value = false
                    jobs = resource.job

                }

                Resource.AuthStatus.LOADING -> {
                    liveLoading.value = true
                    jobs = resource.job

                }

                Resource.AuthStatus.ERROR -> {
                    liveHomeData.value = resource.data
                    liveLoading.value = false
                    jobs = resource.job

                }

            }
        }


    }


    suspend fun getLocation(context: Context?): LiveData<Pair<String, String>?> {


        val fusedLocationClient: FusedLocationProviderClient? =
            context?.let { LocationServices.getFusedLocationProviderClient(it) }

        val result = withContext(Dispatchers.IO) {
            fusedLocationClient?.lastLocation?.let { Tasks.await(it) }
        }

        return result.let {
            return@let MutableLiveData(Pair("${it?.longitude}", "${it?.latitude}"))
        }

    }


    override fun onCleared() {
        super.onCleared()
        jobs?.forEach { if (it.isActive) it.cancel() }
    }


}