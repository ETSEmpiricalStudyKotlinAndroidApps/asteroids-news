package com.klekchyan.asteroidsnews.view.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
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

    private val _shownList = MutableLiveData<ShownList>()
    val shownList: LiveData<ShownList>
        get() = _shownList

    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(shownList){ list ->
        when(list){
            ShownList.ALL -> repository.allAsteroids
            ShownList.FAVORITE -> repository.favoriteAsteroids
        }
    }

    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    val navigateToSpecificAsteroid: LiveData<SimpleAsteroid?>
        get() = _navigateToSpecificAsteroid

    init {
        Timber.d("listViewModel was created")
        _shownList.value = ShownList.ALL
        viewModelScope.launch(IO) {
            repository.refreshAllAsteroids()
        }
    }

    fun showAllAsteroids(){
        Timber.d("showAllAsteroids was called")
        _shownList.value = ShownList.ALL
    }

    fun showFavoriteAsteroids(){
        Timber.d("showFavoriteAsteroids was called")
        _shownList.value = ShownList.FAVORITE
    }

    fun onSpecificAsteroidNavigated(){
        _navigateToSpecificAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid: SimpleAsteroid){
        _navigateToSpecificAsteroid.value = asteroid
    }

    fun onAsteroidSwiped(asteroid: SimpleAsteroid){
        viewModelScope.launch(IO) {
            repository.addAsteroidToFavorite(asteroid)
        }
        Timber.d("onAsteroidSwiped:asteroid is found and added to database")
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