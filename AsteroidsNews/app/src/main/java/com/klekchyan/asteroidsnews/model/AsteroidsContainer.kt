package com.klekchyan.asteroidsnews.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Asteroid(
    val id: Int,
    val name: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: Diameter,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isHazardous: Boolean
): Serializable

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
