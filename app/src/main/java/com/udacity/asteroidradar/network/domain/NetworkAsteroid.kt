package com.udacity.asteroidradar.network.domain

import com.squareup.moshi.JsonClass

/**
 * Data class that represents the NASA NEO asteroid model from the network.
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)