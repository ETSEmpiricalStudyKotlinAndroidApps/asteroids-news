package com.klekchyan.asteroidsnews.list

import android.app.Application
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import kotlinx.coroutines.*
import timber.log.Timber

enum class NasaApiStatus { LOADING, DONE, ERROR }

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = repository.allAsteroids

    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    val navigateToSpecificAsteroid: LiveData<SimpleAsteroid?>
        get() = _navigateToSpecificAsteroid

    fun onSpecificAsteroidNavigated(){
        _navigateToSpecificAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid: SimpleAsteroid){
        _navigateToSpecificAsteroid.value = asteroid
    }

    init {
        Timber.d("listViewModel was created")
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.refreshAllAsteroids()
            }
        }
    }
}

class ListViewModelFactory(private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}