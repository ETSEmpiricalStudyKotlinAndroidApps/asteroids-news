package com.klekchyan.asteroidsnews.specificAsteroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.model.Asteroid

class SpecificAsteroidViewModel(asteroid: Asteroid): ViewModel() {

    private val _asteroid = MutableLiveData<Asteroid>()
    val asteroid: LiveData<Asteroid>
        get() = _asteroid

    init {
        _asteroid.value = asteroid
    }
}