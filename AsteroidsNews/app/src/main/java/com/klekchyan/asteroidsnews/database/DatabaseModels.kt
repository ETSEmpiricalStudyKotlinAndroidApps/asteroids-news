package com.klekchyan.asteroidsnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klekchyan.asteroidsnews.domain.SimpledAsteroid
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
    val closeApproachDate: String
)

fun List<DatabaseSimpleAsteroid>.asDomainSimpleModel(): List<SimpledAsteroid>{
    return map{
        SimpledAsteroid(
            id = it.id,
            name = it.name,
            averageSize = it.averageSize,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachDate
        )
    }
}
