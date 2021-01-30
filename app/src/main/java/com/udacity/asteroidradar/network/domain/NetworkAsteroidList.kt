package com.udacity.asteroidradar.network.domain

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.domain.DatabaseAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidList(val networkAsteroids: List<NetworkAsteroid>)

fun NetworkAsteroidList.asDatabaseModel(): Array<DatabaseAsteroid> {
    return networkAsteroids.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
