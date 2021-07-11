package com.klekchyan.asteroidsnews.view.list

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder
import java.util.*

enum class NasaApiStatus { LOADING, DONE, ERROR }
enum class ShownList { ALL, FAVORITE }

class ListViewModel(application: Application): AndroidViewModel(application) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private val _shownList = MutableLiveData<ShownList>()
    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    private val _navigateToFilterFragment = MutableLiveData<Boolean>(false)

    val shownList: LiveData<ShownList>
        get() = _shownList
    val navigateToSpecificAsteroid: LiveData<SimpleAsteroid?>
        get() = _navigateToSpecificAsteroid
    val navigateToFilterFragment: LiveData<Boolean>
        get() = _navigateToFilterFragment
    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(shownList){ list ->
        when(list){
            ShownList.ALL -> repository.allAsteroids
            ShownList.FAVORITE -> repository.favoriteAsteroids
        }
    }

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

    fun onSpecificAsteroidNavigateDone(){
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

    fun onFilterClicked(){
        _navigateToFilterFragment.value = true
    }

    fun onFilterNavigateDone(){
        _navigateToFilterFragment.value = false
    }

    fun changeDateRange(dateRange: Pair<Long, Long>){
        val startDate: String = dateFormat.format(dateRange.first)
        val endDate: String = dateFormat.format(dateRange.second)
        Timber.d("$startDate - $endDate")
        viewModelScope.launch(IO) {
            repository.refreshAllAsteroids(startDate.toString(), endDate.toString())
        }
    }
}