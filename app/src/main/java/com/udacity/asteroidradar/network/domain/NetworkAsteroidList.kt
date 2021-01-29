package com.udacity.asteroidradar.network.domain

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.Asteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidList(val networkAsteroids: List<NetworkAsteroid>)

/**
 * Convert network asteroids to domain objects.
 */
fun NetworkAsteroidList.asDomainModel(): List<Asteroid> {
    return networkAsteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}