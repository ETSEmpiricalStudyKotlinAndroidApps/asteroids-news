package com.klekchyan.asteroidsnews.network

import com.google.gson.annotations.SerializedName
import com.klekchyan.asteroidsnews.database.DatabaseExtendedAsteroid
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid

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
    val closeApproachData: List<CloseApproachData>,
    @SerializedName("orbital_data")
    val orbitalData: OrbitalData,
    @SerializedName("is_sentry_object")
    val isSentryObject: Boolean
)

data class CloseApproachData(
    @SerializedName("close_approach_date_full")
    val closeApproachDate: String,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @SerializedName("miss_distance")
    val missDistance: MissDistance,
    @SerializedName("orbiting_body")
    val orbitingBody: String
)

data class RelativeVelocity(
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: Double
)

data class MissDistance(
    val astronomical: Double,
    val kilometers: Double
)

data class OrbitalData(
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
    val orbitClass: OrbitClass
)

data class OrbitClass(
    @SerializedName("orbit_class_type")
    val orbitClassType: String,
    @SerializedName("orbit_class_description")
    val orbitClassDescription: String,
    @SerializedName("orbit_class_range")
    val orbitClassRange: String
)

fun List<NetworkExtendedModel>.asSimpledDomainModel(): List<ExtendedAsteroid>{
    return map{
        ExtendedAsteroid(
            id = it.id,
            name = it.name,
            nasaJplUrl = it.nasaJplUrl,
            absoluteMagnitude = it.absoluteMagnitude,
            minDiameterMeters = it.estimatedDiameter.meters.min,
            maxDiameterMeters = it.estimatedDiameter.meters.max,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachData[0].closeApproachDate,
            kilometersPerHourVelocity = it.closeApproachData[0].relativeVelocity.kilometersPerHour,
            astronomicalMissDistance = it.closeApproachData[0].missDistance.astronomical,
            kilometersMissDistance = it.closeApproachData[0].missDistance.kilometers,
            orbitingBody = it.closeApproachData[0].orbitingBody,
            orbitId = it.orbitalData.orbitId,
            firstObservationDate = it.orbitalData.firstObservationDate,
            lastObservationDate = it.orbitalData.lastObservationDate,
            dataArcInDays = it.orbitalData.dataArcInDays,
            orbitUncertainly = it.orbitalData.orbitUncertainly,
            minimumOrbitIntersection = it.orbitalData.minimumOrbitIntersection,
            jupiterTisserandInvariant = it.orbitalData.jupiterTisserandInvariant,
            eccentricity = it.orbitalData.eccentricity,
            semiMajorAxis = it.orbitalData.semiMajorAxis,
            inclination = it.orbitalData.inclination,
            ascendingNodeLongitude = it.orbitalData.ascendingNodeLongitude,
            orbitalPeriod = it.orbitalData.orbitalPeriod,
            perihelionDistance = it.orbitalData.perihelionDistance,
            perihelionArgument = it.orbitalData.perihelionArgument,
            aphelionDistance = it.orbitalData.aphelionDistance,
            perihelionTime = it.orbitalData.perihelionTime,
            meanAnomaly = it.orbitalData.meanAnomaly,
            meanMotion = it.orbitalData.meanMotion,
            equinox = it.orbitalData.equinox,
            orbitClassType = it.orbitalData.orbitClass.orbitClassType,
            orbitClassDescription = it.orbitalData.orbitClass.orbitClassDescription,
            orbitClassRange = it.orbitalData.orbitClass.orbitClassRange,
            isSentryObject = it.isSentryObject
        )
    }
}

fun List<NetworkExtendedModel>.asSimpledDatabaseModel(): List<DatabaseExtendedAsteroid>{
    return map{
        DatabaseExtendedAsteroid(
            id = it.id,
            name = it.name,
            nasaJplUrl = it.nasaJplUrl,
            absoluteMagnitude = it.absoluteMagnitude,
            minDiameterMeters = it.estimatedDiameter.meters.min,
            maxDiameterMeters = it.estimatedDiameter.meters.max,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachData[0].closeApproachDate,
            kilometersPerHourVelocity = it.closeApproachData[0].relativeVelocity.kilometersPerHour,
            astronomicalMissDistance = it.closeApproachData[0].missDistance.astronomical,
            kilometersMissDistance = it.closeApproachData[0].missDistance.kilometers,
            orbitingBody = it.closeApproachData[0].orbitingBody,
            orbitId = it.orbitalData.orbitId,
            firstObservationDate = it.orbitalData.firstObservationDate,
            lastObservationDate = it.orbitalData.lastObservationDate,
            dataArcInDays = it.orbitalData.dataArcInDays,
            orbitUncertainly = it.orbitalData.orbitUncertainly,
            minimumOrbitIntersection = it.orbitalData.minimumOrbitIntersection,
            jupiterTisserandInvariant = it.orbitalData.jupiterTisserandInvariant,
            eccentricity = it.orbitalData.eccentricity,
            semiMajorAxis = it.orbitalData.semiMajorAxis,
            inclination = it.orbitalData.inclination,
            ascendingNodeLongitude = it.orbitalData.ascendingNodeLongitude,
            orbitalPeriod = it.orbitalData.orbitalPeriod,
            perihelionDistance = it.orbitalData.perihelionDistance,
            perihelionArgument = it.orbitalData.perihelionArgument,
            aphelionDistance = it.orbitalData.aphelionDistance,
            perihelionTime = it.orbitalData.perihelionTime,
            meanAnomaly = it.orbitalData.meanAnomaly,
            meanMotion = it.orbitalData.meanMotion,
            equinox = it.orbitalData.equinox,
            orbitClassType = it.orbitalData.orbitClass.orbitClassType,
            orbitClassDescription = it.orbitalData.orbitClass.orbitClassDescription,
            orbitClassRange = it.orbitalData.orbitClass.orbitClassRange,
            isSentryObject = it.isSentryObject
        )
    }
}