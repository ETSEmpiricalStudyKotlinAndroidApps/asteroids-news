package com.klekchyan.asteroidsnews.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao{

    @Query("SELECT * FROM simple_asteroid")
    fun getAllSimpleAsteroids(): LiveData<List<DatabaseSimpleAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSimpleAsteroids(vararg asteroid: DatabaseSimpleAsteroid)

}

@Database(entities = [DatabaseSimpleAsteroid::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}