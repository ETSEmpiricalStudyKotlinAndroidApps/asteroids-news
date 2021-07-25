package com.klekchyan.asteroidsnews.view.specificAsteroid

import android.app.Application
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpecificAsteroidViewModel(val id: Long, application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val asteroid = repository.currentExtendedAsteroid
    val progressIndicatorState = repository.downloadingState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.refreshExtendedAsteroid(id)
            }
        }
    }
}

class SpecificAsteroidViewModelFactory(
        private val asteroidId: Long,
        private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecificAsteroidViewModel::class.java)) {
            return SpecificAsteroidViewModel(asteroidId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}