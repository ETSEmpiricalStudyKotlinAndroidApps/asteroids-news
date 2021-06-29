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
import com.klekchyan.asteroidsnews.network.NasaApi
import com.klekchyan.asteroidsnews.network.asExtendedDomainModel
import com.klekchyan.asteroidsnews.network.asSimpledDatabaseModel
import com.klekchyan.asteroidsnews.utils.getExtendedAsteroidFromResponse
import com.klekchyan.asteroidsnews.utils.getListOfSimpleAsteroidsFromResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class AsteroidsRepository(private val database: AsteroidsDatabase){

    val allAsteroids: LiveData<List<SimpleAsteroid>> = Transformations
        .map(database.asteroidDao.getAllSimpleAsteroids()){ it.asSimpleDomainModel() }

    val favoriteAsteroids: LiveData<List<SimpleAsteroid>> = Transformations
        .map(database.asteroidDao.getFavoriteAsteroids()){ it.asSimpleDomainModelFromFavorite() }

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
                withContext(Dispatchers.Main){
                    currentExtendedAsteroid.value = networkAsteroid.asExtendedDomainModel()
                }
                Timber.d("Refreshing ExtendedAsteroid was successful")
            } catch (e: HttpException){
                Timber.d(e)
            }
        }
    }

    suspend fun refreshAllAsteroids(){
        withContext(Dispatchers.IO){
            try {
                val response = NasaApi.retrofitService.getAllAsteroidsAsync().await()
                val asteroids = getListOfSimpleAsteroidsFromResponse(response)
                database.asteroidDao.insertAllSimpleAsteroids(*asteroids.asSimpledDatabaseModel())
                Timber.d("Refresh all asteroids was successful")
            } catch (e: HttpException){
                Timber.d(e)
            }
        }
    }
}