package com.klekchyan.asteroidsnews.model

import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.utils.getDateFromString
import java.io.Serializable
import java.util.*

data class Asteroid(
    val id: Int,
    val name: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: Diameter,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isHazardous: Boolean,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachData>
): Serializable

data class CloseApproachData(
    @SerializedName("close_approach_date_full")
    val closeApproachDate: String,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @SerializedName("orbiting_body")
    val orbitingBody: String
){
    val date: Date
        get() = closeApproachDate.getDateFromString() ?: Date()
}

data class RelativeVelocity(
    @SerializedName("kilometers_per_second")
    val kilometersPerSecond: Double,
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: Double
)

data class Diameter(
    val kilometers: Kilometer,
    val meters: Meter,
)

data class Kilometer(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
)

data class Meter(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
)

enum class AverageSize{
    SMALL,
    MEDIUM,
    BIG
}
