package com.klekchyan.asteroidsnews.specificAsteroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.klekchyan.asteroidsnews.model.Asteroid

class SpecificAsteroidViewModelFactory(val asteroid: Asteroid): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecificAsteroidViewModel::class.java)) {
            return SpecificAsteroidViewModel(asteroid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}