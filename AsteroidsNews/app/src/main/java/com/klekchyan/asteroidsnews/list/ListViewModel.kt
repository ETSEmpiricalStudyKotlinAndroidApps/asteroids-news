package com.klekchyan.asteroidsnews.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.network.NasaApi
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import kotlinx.coroutines.*
import java.lang.Exception

enum class NasaApiStatus { LOADING, DONE, ERROR }

class ListViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _status = MutableLiveData(NasaApiStatus.DONE)
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _listOfAllAsteroids = MutableLiveData<List<Asteroid>>()
    val listOfAsteroids: LiveData<List<Asteroid>>
        get() = _listOfAllAsteroids

    private val _navigateToSpecificAsteroid = MutableLiveData<Asteroid>()
    val navigateToSpecificAsteroid: LiveData<Asteroid>
        get() = _navigateToSpecificAsteroid

    fun onSpecificAsteroidNavigated(){
        _navigateToSpecificAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToSpecificAsteroid.value = asteroid
    }

    init {
        getAllAsteroids()
    }

    fun getAllAsteroids(startDay: String = "", endDay: String = ""){
        viewModelScope.launch {
            val getAllAsteroidsDeferred = NasaApi.retrofitService.getAllAsteroids(startDay, endDay)
            try{
                _status.value = NasaApiStatus.LOADING
                var asteroids = mutableListOf<Asteroid>()

                withContext(Dispatchers.IO){
                    val response = getAllAsteroidsDeferred.await()
                    asteroids = getListOfAsteroidsFromResponse(response)
                        .sortedBy { it.closeApproachData[0].date }
                        .toMutableList()
                }

                _status.value = NasaApiStatus.DONE
                _listOfAllAsteroids.value = asteroids
            } catch (e: Exception){
                _status.value = NasaApiStatus.ERROR
                Log.w("ListViewModel", "${e.message}")
                _listOfAllAsteroids.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}