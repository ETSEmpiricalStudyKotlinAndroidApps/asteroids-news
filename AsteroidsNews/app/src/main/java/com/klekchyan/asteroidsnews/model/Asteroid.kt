package com.klekchyan.asteroidsnews.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.utils.getDateFromString
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Asteroid(
    val id: Int,
    val name: String,
    @SerializedName("nasa_jpl_url")
    val nasaJplUrl: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: Diameter,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isHazardous: Boolean,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachData>,
    @SerializedName("is_sentry_object")
    val isSentryObject: Boolean
): Parcelable

@Parcelize
data class CloseApproachData(
    @SerializedName("close_approach_date_full")
    val closeApproachDate: String,
    @SerializedName("epoch_date_close_approach")
    val epochDateCloseApproach: Long,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @SerializedName("miss_distance")
    val missDistance: MissDistance,
    @SerializedName("orbiting_body")
    val orbitingBody: String
): Parcelable {
    val date: Date
        get() = closeApproachDate.getDateFromString() ?: Date()
}

@Parcelize
data class RelativeVelocity(
    @SerializedName("kilometers_per_second")
    val kilometersPerSecond: Double,
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: Double,
    @SerializedName("miles_per_hour")
    val milePerHour: Double
): Parcelable

@Parcelize
data class MissDistance(
    val astronomical: Double,
    val lunar: Double,
    val kilometers: Double,
    val miles: Double
): Parcelable

@Parcelize
data class Diameter(
    val kilometers: Kilometer,
    val meters: Meter,
    val miles: Mile,
    val feet: Feet
): Parcelable

@Parcelize
data class Mile(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
): Parcelable

@Parcelize
data class Feet(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
): Parcelable

@Parcelize
data class Kilometer(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
): Parcelable

@Parcelize
data class Meter(
    @SerializedName("estimated_diameter_min")
    val min: Double,
    @SerializedName("estimated_diameter_max")
    val max: Double
): Parcelable

enum class AverageSize{
    SMALL,
    MEDIUM,
    BIG
}
