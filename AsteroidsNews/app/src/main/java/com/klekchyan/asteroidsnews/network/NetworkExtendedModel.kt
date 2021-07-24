package com.klekchyan.asteroidsnews.network

import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.domain.CloseApproachData
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid
import com.klekchyan.asteroidsnews.utils.getDateFromNasaApiResponseFormat

data class NetworkExtendedModel(
    val id: Long,
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
    val networkCloseApproachData: List<NetworkCloseApproachData>,
    @SerializedName("orbital_data")
    val networkOrbitalData: NetworkOrbitalData,
    @SerializedName("is_sentry_object")
    val isSentryObject: Boolean
)

data class NetworkCloseApproachData(
    @SerializedName("close_approach_date_full")
    val closeApproachDate: String,
    @SerializedName("relative_velocity")
    val relativeVelocity: NetworkRelativeVelocity,
    @SerializedName("miss_distance")
    val networkMissDistance: NetworkMissDistance,
    @SerializedName("orbiting_body")
    val orbitingBody: String
)

data class NetworkRelativeVelocity(
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: Double
)

data class NetworkMissDistance(
    val astronomical: Double,
    val kilometers: Double
)

data class NetworkOrbitalData(
    @SerializedName("orbit_id")
    val orbitId: String,
    @SerializedName("first_observation_date")
    val firstObservationDate: String,
    @SerializedName("last_observation_date")
    val lastObservationDate: String,
    @SerializedName("data_arc_in_days")
    val dataArcInDays: String,
    @SerializedName("orbit_uncertainty")
    val orbitUncertainly: String,
    @SerializedName("minimum_orbit_intersection")
    val minimumOrbitIntersection: String,
    @SerializedName("jupiter_tisserand_invariant")
    val jupiterTisserandInvariant: String,
    val eccentricity: String,
    @SerializedName("semi_major_axis")
    val semiMajorAxis: String,
    val inclination: String,
    @SerializedName("ascending_node_longitude")
    val ascendingNodeLongitude: String,
    @SerializedName("orbital_period")
    val orbitalPeriod: String,
    @SerializedName("perihelion_distance")
    val perihelionDistance: String,
    @SerializedName("perihelion_argument")
    val perihelionArgument: String,
    @SerializedName("aphelion_distance")
    val aphelionDistance: String,
    @SerializedName("perihelion_time")
    val perihelionTime: String,
    @SerializedName("mean_anomaly")
    val meanAnomaly: String,
    @SerializedName("mean_motion")
    val meanMotion: String,
    val equinox: String,
    @SerializedName("orbit_class")
    val networkOrbitClass: NetworkOrbitClass
)

data class NetworkOrbitClass(
    @SerializedName("orbit_class_type")
    val orbitClassType: String,
    @SerializedName("orbit_class_description")
    val orbitClassDescription: String,
    @SerializedName("orbit_class_range")
    val orbitClassRange: String
)

fun NetworkExtendedModel.asExtendedDomainModel(): ExtendedAsteroid{
    return ExtendedAsteroid(
            id = this.id,
            name = this.name,
            nasaJplUrl = this.nasaJplUrl,
            absoluteMagnitude = this.absoluteMagnitude,
            minDiameterMeters = this.estimatedDiameter.meters.min,
            maxDiameterMeters = this.estimatedDiameter.meters.max,
            isHazardous = this.isHazardous,
            closeApproachData = this.networkCloseApproachData.map { networkCloseApproachData ->
                CloseApproachData(
                    closeApproachDate = networkCloseApproachData.closeApproachDate.getDateFromNasaApiResponseFormat(),
                    kilometersPerHourVelocity = networkCloseApproachData.relativeVelocity.kilometersPerHour,
                    astronomicalMissDistance = networkCloseApproachData.networkMissDistance.astronomical,
                    kilometersMissDistance = networkCloseApproachData.networkMissDistance.kilometers,
                    orbitingBody = networkCloseApproachData.orbitingBody
                ) },
            orbitId = this.networkOrbitalData.orbitId,
            firstObservationDate = this.networkOrbitalData.firstObservationDate,
            lastObservationDate = this.networkOrbitalData.lastObservationDate,
            dataArcInDays = this.networkOrbitalData.dataArcInDays,
            orbitUncertainly = this.networkOrbitalData.orbitUncertainly,
            minimumOrbitIntersection = this.networkOrbitalData.minimumOrbitIntersection,
            jupiterTisserandInvariant = this.networkOrbitalData.jupiterTisserandInvariant,
            eccentricity = this.networkOrbitalData.eccentricity,
            semiMajorAxis = this.networkOrbitalData.semiMajorAxis,
            inclination = this.networkOrbitalData.inclination,
            ascendingNodeLongitude = this.networkOrbitalData.ascendingNodeLongitude,
            orbitalPeriod = this.networkOrbitalData.orbitalPeriod,
            perihelionDistance = this.networkOrbitalData.perihelionDistance,
            perihelionArgument = this.networkOrbitalData.perihelionArgument,
            aphelionDistance = this.networkOrbitalData.aphelionDistance,
            perihelionTime = this.networkOrbitalData.perihelionTime,
            meanAnomaly = this.networkOrbitalData.meanAnomaly,
            meanMotion = this.networkOrbitalData.meanMotion,
            equinox = this.networkOrbitalData.equinox,
            orbitClassType = this.networkOrbitalData.networkOrbitClass.orbitClassType,
            orbitClassDescription = this.networkOrbitalData.networkOrbitClass.orbitClassDescription,
            orbitClassRange = this.networkOrbitalData.networkOrbitClass.orbitClassRange,
            isSentryObject = this.isSentryObject
    )
}
