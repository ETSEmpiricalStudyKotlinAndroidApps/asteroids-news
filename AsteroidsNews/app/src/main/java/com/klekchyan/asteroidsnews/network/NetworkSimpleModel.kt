package com.klekchyan.asteroidsnews.network

import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.database.DatabaseSimpleAsteroid
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.utils.getDateFromString

data class NetworkSimpleAsteroid(
    val id: Long,
    val name: String,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: Diameter,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isHazardous: Boolean,
    @SerializedName("close_approach_data")
    val closeApproachData: List<SimplifiedCloseApproachData>
) {
    val averageSize: AverageSize
        get() = when((estimatedDiameter.meters.min + estimatedDiameter.meters.max) / 2){
                in 0.0..100.0 -> AverageSize.SMALL
                in 100.1..500.0 -> AverageSize.MEDIUM
                else -> AverageSize.BIG
        }
}

data class SimplifiedCloseApproachData(
    @SerializedName("close_approach_date_full")
    val closeApproachDate: String,
    @SerializedName("orbiting_body")
    val orbitingBody: String
)

data class Diameter(val meters: Meter)

data class Meter(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
)

enum class AverageSize(val numericalValue: Float){ SMALL(0f), MEDIUM(1f), BIG(2f) }

fun List<NetworkSimpleAsteroid>.asSimpledDomainModel(): List<SimpleAsteroid>{
    return map{
        SimpleAsteroid(
            id = it.id,
            name = it.name,
            averageSize = it.averageSize,
            isHazardous = it.isHazardous,
            closeApproachDate = getDateFromString(it.closeApproachData[0].closeApproachDate),
            orbitingBody = it.closeApproachData[0].orbitingBody
        )
    }
}

fun List<NetworkSimpleAsteroid>.asSimpledDatabaseModel(): Array<DatabaseSimpleAsteroid>{
    return map{
        DatabaseSimpleAsteroid(
            id = it.id,
            name = it.name,
            averageSize = it.averageSize,
            isHazardous = it.isHazardous,
            closeApproachDate = getDateFromString(it.closeApproachData[0].closeApproachDate).time,
            orbitingBody = it.closeApproachData[0].orbitingBody
        )
    }.toTypedArray()
}

