package com.klekchyan.asteroidsnews.list

import android.app.Application
import androidx.lifecycle.*
import com.klekchyan.asteroidsnews.database.getDatabase
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.repository.AsteroidsRepository
import kotlinx.coroutines.*

enum class NasaApiStatus { LOADING, DONE, ERROR }

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private val _status = MutableLiveData(NasaApiStatus.DONE)
//    val status: LiveData<NasaApiStatus>
//        get() = _status

    //private val _listOfAllAsteroids = MutableLiveData<List<SimpleAsteroid>>()
    val listOfAsteroids: LiveData<List<SimpleAsteroid>> = repository.asteroids
        //get() = _listOfAllAsteroids

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
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.refreshData()
            }
        }
    }

//    fun getAllAsteroids(startDay: String = "", endDay: String = ""){
//        viewModelScope.launch {
//            val getAllAsteroidsDeferred = NasaApi.retrofitService.getAllAsteroids(startDay, endDay)
//            try{
//                _status.value = NasaApiStatus.LOADING
//                var asteroids = mutableListOf<Asteroid>()
//
//                withContext(Dispatchers.IO){
//                    val response = getAllAsteroidsDeferred.await()
//                    asteroids = getListOfAsteroidsFromResponse(response)
//                        .sortedBy { it.closeApproachData[0].date }
//                        .toMutableList()
//                }
//
//                _status.value = NasaApiStatus.DONE
//                _listOfAllAsteroids.value = asteroids
//            } catch (e: Exception){
//                _status.value = NasaApiStatus.ERROR
//                Log.w("ListViewModel", "${e.message}")
//                _listOfAllAsteroids.value = ArrayList()
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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