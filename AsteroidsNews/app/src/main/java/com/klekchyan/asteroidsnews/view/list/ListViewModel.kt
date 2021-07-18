package com.klekchyan.asteroidsnews.view.list

import android.app.Application
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import com.klekchyan.asteroidsnews.repository.Filter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.util.Pair
import com.klekchyan.asteroidsnews.network.AverageSize

enum class NasaApiStatus { LOADING, DONE, ERROR }
enum class ShownList { ALL, FAVORITE }

class ListViewModel(application: Application): AndroidViewModel(application) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private val _shownList = MutableLiveData<ShownList>()
    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    private val _navigateToFilterFragment = MutableLiveData<Boolean>(false)
    private var currentDateRange: Pair<Long, Long>? = null

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

    fun changeDateRange(newDateRange: Pair<Long, Long>){

        val newStartDate = dateFormat.format(newDateRange.first)
        val newEndDate = dateFormat.format(newDateRange.second)

        //Crutch!! It's needed because changeDateRange is called without any date changing
        // and execute repository.refreshAllAsteroids()
        if(currentDateRange != null){
            val currentStartDate = dateFormat.format(currentDateRange!!.first)
            val currentEndDate = dateFormat.format(currentDateRange!!.second)
            if(newStartDate == currentStartDate && newEndDate == currentEndDate) return
        }

        Timber.d("changeDateRange $newDateRange")
        viewModelScope.launch(IO) {
            repository.refreshAllAsteroids(newStartDate, newEndDate)
        }
        currentDateRange = newDateRange
    }

    fun changeFilterByHazardous(isHazardous: Boolean) {
        var filter = repository.currentFilterInstance
        if(filter != null){
            filter.onlyHazardous = isHazardous
        } else {
            filter = Filter(onlyHazardous = isHazardous)
        }
        Timber.d("changeFilterByHazardous $filter")
        repository.setFilter(filter)
    }

    fun changeFilterByAverageSize(sizeRange: Pair<AverageSize, AverageSize>) {
        var filter = repository.currentFilterInstance
        if(filter != null){
            filter.sizeRange = sizeRange
        } else {
            filter = Filter(sizeRange = sizeRange)
        }
        Timber.d("changeFilterByAverageSize $filter")
        repository.setFilter(filter)
    }
}