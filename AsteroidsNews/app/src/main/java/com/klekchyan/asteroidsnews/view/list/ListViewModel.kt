package com.klekchyan.asteroidsnews.view.list

import android.app.Application
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.domain.asDatabaseFavoriteModel
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher
import timber.log.Timber

enum class NasaApiStatus { LOADING, DONE, ERROR }
enum class ShownList { ALL, FAVORITE }

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = repository.allAsteroids
    val listOfFavoriteAsteroids: LiveData<List<SimpleAsteroid>> = repository.favoriteAsteroids

    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    val navigateToSpecificAsteroid: LiveData<SimpleAsteroid?>
        get() = _navigateToSpecificAsteroid

    private val _shownList = MutableLiveData<ShownList>()
    val shownList: LiveData<ShownList>
        get() = _shownList

    init {
        _shownList.value = ShownList.ALL
        Timber.d("listViewModel was created")
        viewModelScope.launch(IO) {
            repository.refreshAllAsteroids()
        }
    }

    fun showAllAsteroids(){
        _shownList.value = ShownList.ALL
    }

    fun showFavoriteAsteroids(){
        _shownList.value = ShownList.FAVORITE
    }

    fun onSpecificAsteroidNavigated(){
        _navigateToSpecificAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid: SimpleAsteroid){
        _navigateToSpecificAsteroid.value = asteroid
    }

    fun onAsteroidSwiped(id: Long){
        val asteroid = listOfAsteroids.value!!.first { it.id == id }
        viewModelScope.launch(IO) {
            repository.addAsteroidToFavorite(asteroid)
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