package com.klekchyan.asteroidsnews.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.utils.RequestType
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import com.klekchyan.asteroidsnews.utils.getObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {
    private val _listOfAllAsteroids = MutableLiveData<MutableList<Asteroid>>()
    private val _filteredListOfAsteroids = MutableLiveData<MutableList<Asteroid>>()
    val listOfAsteroids: LiveData<MutableList<Asteroid>>
        get() = _filteredListOfAsteroids

    private val _isHazardous = MutableLiveData<Boolean>()
    val isHazardous: LiveData<Boolean>
        get() = _isHazardous

    init {
        getAllAsteroids()
        _isHazardous.value = false
    }

    @SuppressLint("CheckResult")
    fun getAllAsteroids(){
        val observer = getObserver(RequestType.ALL_ASTEROIDS)
                .map { getListOfAsteroidsFromResponse(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        observer.subscribe({ asteroids ->
            _listOfAllAsteroids.value = asteroids
            _filteredListOfAsteroids.value = _listOfAllAsteroids.value
        }, {
            Log.e("Parsing", it.message.toString())
        })
    }

    fun getFilteredAsteroids(isHazardous: Boolean){
        if(!isHazardous) {
            _filteredListOfAsteroids.value = _listOfAllAsteroids.value
            _isHazardous.value = false
        } else {
            _filteredListOfAsteroids.value = _listOfAllAsteroids.value!!.filter { it.isHazardous }.toMutableList()
            _isHazardous.value = true
        }
    }
}