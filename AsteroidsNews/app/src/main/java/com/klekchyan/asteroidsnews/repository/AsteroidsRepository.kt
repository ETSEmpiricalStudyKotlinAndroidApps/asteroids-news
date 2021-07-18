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

class AsteroidsRepository(private val database: AsteroidsDatabase){

    private val _filter = MutableLiveData(Filter())
    val filter: LiveData<Filter>
        get() = _filter
    val currentFilterInstance: Filter?
        get() = _filter.value

    val allAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(filter){ filter ->
        Transformations.map(database.asteroidDao.getAllSimpleAsteroids()){ it.asSimpleDomainModel()
            .filter { asteroid ->
                (asteroid.averageSize in filter.sizeRange.first..filter.sizeRange.second) &&
                asteroid.isHazardous == if(filter.onlyHazardous) filter.onlyHazardous else asteroid.isHazardous

            } }
    }

    val favoriteAsteroids: LiveData<List<SimpleAsteroid>> = Transformations.switchMap(filter){ filter ->
        Transformations.map(database.asteroidDao.getFavoriteAsteroids()){ it.asSimpleDomainModelFromFavorite()
            .filter { asteroid ->
                (asteroid.averageSize in filter.sizeRange.first..filter.sizeRange.second) &&
                        asteroid.isHazardous == if(filter.onlyHazardous) filter.onlyHazardous else asteroid.isHazardous

            } }
    }

    fun setFilter(filter: Filter){
        _filter.value = filter
    }

    val currentExtendedAsteroid = MutableLiveData<ExtendedAsteroid>()

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

    suspend fun refreshExtendedAsteroid(id: Long) {
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.retrofitService.getSpecificAsteroidAsync(id.toInt()).await()
                val networkAsteroid = getExtendedAsteroidFromResponse(response)
                currentExtendedAsteroid.postValue(networkAsteroid.asExtendedDomainModel())
                Timber.d("Refreshing ExtendedAsteroid was successful")
            } catch (e: Exception){
                Timber.d(e)
            }
        }
    }

    suspend fun refreshAllAsteroids(startDate: String = "", endDate: String = ""){
        withContext(Dispatchers.IO){
            try {
                val response = NasaApi.retrofitService.getAllAsteroidsAsync(startDate, endDate).await()
                val asteroids = getListOfSimpleAsteroidsFromResponse(response)
                database.asteroidDao.deleteAllFromSimpleAsteroid()
                database.asteroidDao.insertAllSimpleAsteroids(*asteroids.asSimpledDatabaseModel())
                Timber.d("Refresh all asteroids was successful")
            } catch (e: Exception){
                Timber.d(e)
            }
        }
    }
}