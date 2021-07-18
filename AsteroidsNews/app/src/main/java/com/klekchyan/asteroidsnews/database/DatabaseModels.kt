package com.klekchyan.asteroidsnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.network.AverageSize

@Entity(tableName = "simple_asteroid")
data class DatabaseSimpleAsteroid(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "average_size")
    val averageSize: AverageSize,
    @ColumnInfo(name = "is_hazardous")
    val isHazardous: Boolean,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "orbiting_body")
    val orbitingBody: String
)

@Entity(tableName = "favorite_asteroid")
data class DatabaseFavoriteAsteroid(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "average_size")
    val averageSize: AverageSize,
    @ColumnInfo(name = "is_hazardous")
    val isHazardous: Boolean,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "orbiting_body")
    val orbitingBody: String
)

fun List<DatabaseSimpleAsteroid>.asSimpleDomainModel(): List<SimpleAsteroid>{
    return map{
        SimpleAsteroid(
            id = it.id,
            name = it.name,
            averageSize = it.averageSize,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachDate,
            orbitingBody = it.orbitingBody
        )
    }
}

fun List<DatabaseFavoriteAsteroid>.asSimpleDomainModelFromFavorite(): List<SimpleAsteroid>{
    return map{
        SimpleAsteroid(
            id = it.id,
            name = it.name,
            averageSize = it.averageSize,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachDate,
            orbitingBody = it.orbitingBody
        )
    }
}
