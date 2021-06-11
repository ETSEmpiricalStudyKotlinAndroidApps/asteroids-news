package com.klekchyan.asteroidsnews.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.network.NasaApi
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ListViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listOfAllAsteroids = MutableLiveData<List<Asteroid>>()
    private val _filteredListOfAsteroids = MutableLiveData<List<Asteroid>>()
    val listOfAsteroids: LiveData<List<Asteroid>>
        get() = _filteredListOfAsteroids

    private val _isHazardous = MutableLiveData<Boolean>()
    val isHazardous: LiveData<Boolean>
        get() = _isHazardous

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
        _isHazardous.value = false
    }

    fun getAllAsteroids(startDay: String = "", endDay: String = ""){
        viewModelScope.launch {
            val getAllAsteroidsDeferred = NasaApi.retrofitService.getAllAsteroids(startDay, endDay)
            try{
                val response = getAllAsteroidsDeferred.await()
                Log.w("ListViewModel", "$response")
                val asteroids = getListOfAsteroidsFromResponse(response)
                _listOfAllAsteroids.value = asteroids
                _filteredListOfAsteroids.value = _listOfAllAsteroids.value
            } catch (e: Exception){
                Log.w("ListViewModel", "${e.message}")
                _listOfAllAsteroids.value = ArrayList()
                _filteredListOfAsteroids.value = _listOfAllAsteroids.value
            }
        }
    }

    fun getFilteredAsteroids(isHazardous: Boolean){
        if(!isHazardous) {
            _filteredListOfAsteroids.value = _listOfAllAsteroids.value
            _isHazardous.value = false
        } else {
            _filteredListOfAsteroids.value = _listOfAllAsteroids.value!!
                .filter { it.isHazardous }
                .toMutableList()
            _isHazardous.value = true
        }
    }
}