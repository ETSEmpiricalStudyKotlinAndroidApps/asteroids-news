package com.klekchyan.asteroidsnews.specificAsteroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SpecificAsteroidViewModel(val id: Long): ViewModel() {

    private val _asteroidId = MutableLiveData<Long>()
    val asteroidId: LiveData<Long>
        get() = _asteroidId

    init {
        _asteroidId.value = this.id
    }
}

class SpecificAsteroidViewModelFactory(val asteroidId: Long): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecificAsteroidViewModel::class.java)) {
            return SpecificAsteroidViewModel(asteroidId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}