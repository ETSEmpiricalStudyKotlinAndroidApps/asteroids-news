package com.klekchyan.asteroidsnews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.klekchyan.asteroidsnews.database.AsteroidsDatabase
import com.klekchyan.asteroidsnews.database.asSimpleDomainModel
import com.klekchyan.asteroidsnews.database.asSimpleDomainModelFromFavorite
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.domain.asDatabaseFavoriteModel
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.network.NasaApi
import com.klekchyan.asteroidsnews.network.asExtendedDomainModel
import com.klekchyan.asteroidsnews.network.asSimpledDatabaseModel
import com.klekchyan.asteroidsnews.utils.getExtendedAsteroidFromResponse
import com.klekchyan.asteroidsnews.utils.getListOfSimpleAsteroidsFromResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import androidx.core.util.Pair

data class Filter(
    var sizeRange: Pair<AverageSize, AverageSize> = Pair(AverageSize.SMALL, AverageSize.BIG),
    var onlyHazardous: Boolean = false,
)

enum class DownloadingState{ START, FINISH, FAILURE }

class AsteroidsRepository(private val database: AsteroidsDatabase){

    private val filter = MutableLiveData(Filter())
    private val _downloadingState = MutableLiveData<DownloadingState>()
    val downloadingState: LiveData<DownloadingState>
        get() = _downloadingState
    val currentFilterInstance: Filter?
        get() = filter.value

    init {
        Timber.d("AsteroidsRepository was created")
    }

    val allAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(filter){ filter ->
        Transformations.map(database.asteroidDao.getAllSimpleAsteroids()){ it.asSimpleDomainModel()
            .filter { asteroid ->
                (asteroid.averageSize in filter.sizeRange.first..filter.sizeRange.second) &&
                asteroid.isHazardous == if(filter.onlyHazardous) filter.onlyHazardous else asteroid.isHazardous }
            .sortedBy { asteroid -> asteroid.closeApproachDate }
        }
    }

    val favoriteAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(filter){ filter ->
        Transformations.map(database.asteroidDao.getFavoriteAsteroids()){ it.asSimpleDomainModelFromFavorite()
            .filter { asteroid ->
                (asteroid.averageSize in filter.sizeRange.first..filter.sizeRange.second) &&
                asteroid.isHazardous == if(filter.onlyHazardous) filter.onlyHazardous else asteroid.isHazardous }
            .sortedBy { asteroid -> asteroid.closeApproachDate }
        }
    }

    val currentExtendedAsteroid = MutableLiveData<ExtendedAsteroid>()

    fun setFilter(filter: Filter){
        this.filter.value = filter
    }

    suspend fun addAsteroidToFavorite(asteroid: SimpleAsteroid){
        withContext(Dispatchers.IO){
            try{
                database.asteroidDao.insertFavoriteAsteroid(asteroid.asDatabaseFavoriteModel())
                Timber.d("Adding to favorite was successful")
            } catch (e: Exception){
                Timber.d(e)
            }
        }
    }

    suspend fun deleteAsteroidFromFavorite(asteroid: SimpleAsteroid){
        withContext(Dispatchers.IO){
            try{
                database.asteroidDao.deleteFavoriteAsteroid(asteroid.asDatabaseFavoriteModel())
                Timber.d("Deleting from favorite was successful")
            } catch (e: Exception){
                Timber.d(e)
            }
        }
    }

    suspend fun refreshExtendedAsteroid(id: Long) {
        setDownloadingState(DownloadingState.START)
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.nasaApiService.getSpecificAsteroidAsync(id.toInt()).await()
                val networkAsteroid = getExtendedAsteroidFromResponse(response)
                currentExtendedAsteroid.postValue(networkAsteroid.asExtendedDomainModel())
                Timber.d("Refreshing ExtendedAsteroid was successful")
                setDownloadingState(DownloadingState.FINISH)
            } catch (e: Exception){
                setDownloadingState(DownloadingState.FAILURE)
                Timber.d(e)
            }
        }
    }

    suspend fun refreshAllAsteroids(startDate: String = "", endDate: String = ""){
        setDownloadingState(DownloadingState.START)
        try {
            val response = NasaApi.nasaApiService.getAllAsteroidsAsync(startDate, endDate).await()
            val asteroids = getListOfSimpleAsteroidsFromResponse(response)
            database.asteroidDao.refreshAsteroids(*asteroids.asSimpledDatabaseModel())
            Timber.d("Refresh all asteroids was successful")
        } catch (e: Exception){
            setDownloadingState(DownloadingState.FAILURE)
            Timber.d(e)
        }
        setDownloadingState(DownloadingState.FINISH)
    }

    private suspend fun setDownloadingState(state: DownloadingState){
        withContext(Dispatchers.Main){
            _downloadingState.value = state
        }
    }
}