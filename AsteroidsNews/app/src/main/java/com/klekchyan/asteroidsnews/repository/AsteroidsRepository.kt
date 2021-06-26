package com.klekchyan.asteroidsnews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.klekchyan.asteroidsnews.database.AsteroidsDatabase
import com.klekchyan.asteroidsnews.database.asSimpleDomainModel
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.network.NasaApi
import com.klekchyan.asteroidsnews.network.asSimpledDatabaseModel
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase){

    val asteroids: LiveData<List<SimpleAsteroid>> = Transformations
        .map(database.asteroidDao.getAllSimpleAsteroids()){ it.asSimpleDomainModel() }

    suspend fun refreshData(){
        withContext(Dispatchers.IO){
            val response = NasaApi.retrofitService.getAllAsteroids().await()
            val asteroids = getListOfAsteroidsFromResponse(response)
            database.asteroidDao.insertAllSimpleAsteroids(*asteroids.asSimpledDatabaseModel())
        }
    }
}