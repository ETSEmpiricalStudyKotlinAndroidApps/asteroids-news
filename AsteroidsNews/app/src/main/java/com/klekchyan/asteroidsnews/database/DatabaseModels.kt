package com.klekchyan.asteroidsnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid
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

@Entity(tableName = "extendedAsteroid")
data class DatabaseExtendedAsteroid(
    @PrimaryKey
    val id: Long,
    val name: String,
    val nasaJplUrl: String,
    val absoluteMagnitude: Double,
    val minDiameterMeters: Double,
    val maxDiameterMeters: Double,
    val isHazardous: Boolean,
    val closeApproachDate: String,
    val kilometersPerHourVelocity: Double,
    val astronomicalMissDistance: Double,
    val kilometersMissDistance: Double,
    val orbitingBody: String,
    val orbitId: String,
    val firstObservationDate: String,
    val lastObservationDate: String,
    val dataArcInDays: String,
    val orbitUncertainly: String,
    val minimumOrbitIntersection: String,
    val jupiterTisserandInvariant: String,
    val eccentricity: String,
    val semiMajorAxis: String,
    val inclination: String,
    val ascendingNodeLongitude: String,
    val orbitalPeriod: String,
    val perihelionDistance: String,
    val perihelionArgument: String,
    val aphelionDistance: String,
    val perihelionTime: String,
    val meanAnomaly: String,
    val meanMotion: String,
    val equinox: String,
    val orbitClassType: String,
    val orbitClassDescription: String,
    val orbitClassRange: String,
    val isSentryObject: Boolean
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

fun List<DatabaseExtendedAsteroid>.asDomainExtendedModel(): List<ExtendedAsteroid>{
    return map{
        ExtendedAsteroid(
            id = it.id,
            name = it.name,
            nasaJplUrl = it.nasaJplUrl,
            absoluteMagnitude = it.absoluteMagnitude,
            minDiameterMeters = it.minDiameterMeters,
            maxDiameterMeters = it.maxDiameterMeters,
            isHazardous = it.isHazardous,
            closeApproachDate = it.closeApproachDate,
            kilometersPerHourVelocity = it.kilometersPerHourVelocity,
            astronomicalMissDistance = it.astronomicalMissDistance,
            kilometersMissDistance = it.kilometersMissDistance,
            orbitingBody = it.orbitingBody,
            orbitId = it.orbitId,
            firstObservationDate = it.firstObservationDate,
            lastObservationDate = it.lastObservationDate,
            dataArcInDays = it.dataArcInDays,
            orbitUncertainly = it.orbitUncertainly,
            minimumOrbitIntersection = it.minimumOrbitIntersection,
            jupiterTisserandInvariant = it.jupiterTisserandInvariant,
            eccentricity = it.eccentricity,
            semiMajorAxis = it.semiMajorAxis,
            inclination = it.inclination,
            ascendingNodeLongitude = it.ascendingNodeLongitude,
            orbitalPeriod = it.orbitalPeriod,
            perihelionDistance = it.perihelionDistance,
            perihelionArgument = it.perihelionArgument,
            aphelionDistance = it.aphelionDistance,
            perihelionTime = it.perihelionTime,
            meanAnomaly = it.meanAnomaly,
            meanMotion = it.meanMotion,
            equinox = it.equinox,
            orbitClassType = it.orbitClassType,
            orbitClassDescription = it.orbitClassDescription,
            orbitClassRange = it.orbitClassRange,
            isSentryObject = it.isSentryObject
        )
    }
}
