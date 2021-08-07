package com.klekchyan.asteroidsnews.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao{

    @Transaction
    fun refreshAsteroids(vararg asteroid: DatabaseSimpleAsteroid){
        deleteAllFromSimpleAsteroid()
        insertAllSimpleAsteroids(*asteroid)
    }

    @Query("SELECT * FROM simple_asteroid")
    fun getAllSimpleAsteroids(): LiveData<List<DatabaseSimpleAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSimpleAsteroids(vararg asteroid: DatabaseSimpleAsteroid)

    @Query("DELETE FROM simple_asteroid")
    fun deleteAllFromSimpleAsteroid()

    @Query("SELECT * FROM favorite_asteroid")
    fun getFavoriteAsteroids(): LiveData<List<DatabaseFavoriteAsteroid>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertFavoriteAsteroid(asteroid: DatabaseFavoriteAsteroid)

    @Delete
    fun deleteFavoriteAsteroid(asteroid: DatabaseFavoriteAsteroid)

}

@Database(entities = [DatabaseSimpleAsteroid::class, DatabaseFavoriteAsteroid::class], version = 4)
abstract class AsteroidsDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(context.applicationContext, AsteroidsDatabase::class.java, "asteroids")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
