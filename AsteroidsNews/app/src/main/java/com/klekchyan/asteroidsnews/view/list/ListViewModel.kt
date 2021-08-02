package com.klekchyan.asteroidsnews.view.list

import android.app.Application
import androidx.core.util.Pair
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import com.klekchyan.asteroidsnews.repository.DownloadingState
import com.klekchyan.asteroidsnews.repository.Filter
import com.klekchyan.asteroidsnews.utils.getDateStringForNasaApiRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

enum class ShownList { ALL, FAVORITE }

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private val _shownList = MutableLiveData<ShownList>()
    private val _navigateToSpecificAsteroid = MutableLiveData<SimpleAsteroid?>()
    private val _navigateToFilterFragment = MutableLiveData<Boolean>()
    private val _navigateToInfoFragment = MutableLiveData<Boolean>()
    private var currentDateRange: Pair<Long, Long> =
        Pair(System.currentTimeMillis(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(6))

    val shownList: LiveData<ShownList>
        get() = _shownList
    val navigateToSpecificAsteroid: LiveData<SimpleAsteroid?>
        get() = _navigateToSpecificAsteroid
    val navigateToFilterFragment: LiveData<Boolean>
        get() = _navigateToFilterFragment
    val navigateToInfoFragment: LiveData<Boolean>
        get() = _navigateToInfoFragment
    val progressIndicatorState: LiveData<DownloadingState> = repository.downloadingState
    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(shownList){ list ->
        when(list){
            ShownList.ALL -> repository.allAsteroids
            ShownList.FAVORITE -> repository.favoriteAsteroids
            else -> throw ClassCastException("Unknown list $list")
        }
    }
    val isEmptyList: LiveData<Boolean> = Transformations.map(listOfAsteroids){
        it.isEmpty()
    }

    init {
        Timber.d("listViewModel was created")
        _shownList.value = ShownList.ALL
        startRefreshing()
    }

    fun startRefreshing(startDate: String = "", endDate: String = ""){
        Timber.d("startRefreshing $startDate - $endDate")
        viewModelScope.launch(IO) {
            repository.refreshAllAsteroids(startDate, endDate)
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

    fun onInfoClicked(){
        _navigateToInfoFragment.value = true
    }

    fun onInfoNavigateDone(){
        _navigateToInfoFragment.value = false
    }

    fun onFilterClicked(){
        _navigateToFilterFragment.value = true
    }

    fun onFilterNavigateDone(){
        _navigateToFilterFragment.value = false
    }

    fun onAddAsteroidToFavoriteClicked(asteroid: SimpleAsteroid){
        viewModelScope.launch(IO) {
            repository.addAsteroidToFavorite(asteroid)
        }
    }

    fun onDeleteAsteroidFromFavoriteClicked(asteroid: SimpleAsteroid){
        viewModelScope.launch(IO) {
            repository.deleteAsteroidFromFavorite(asteroid)
        }
    }

    fun changeDateRange(newDateRange: Pair<Long, Long>){

        val newStartDate = newDateRange.first.getDateStringForNasaApiRequest()
        val newEndDate = newDateRange.second.getDateStringForNasaApiRequest()

        //Crutch!! It's needed because changeDateRange is called without any date changing
        // and execute repository.refreshAllAsteroids()
        val currentStartDate = currentDateRange.first.getDateStringForNasaApiRequest()
        val currentEndDate = currentDateRange.second.getDateStringForNasaApiRequest()
        if(newStartDate == currentStartDate && newEndDate == currentEndDate) return

        Timber.d("changeDateRange $newDateRange")
        startRefreshing(newStartDate, newEndDate)
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