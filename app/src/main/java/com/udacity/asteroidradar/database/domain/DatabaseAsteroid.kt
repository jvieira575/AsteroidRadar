package com.udacity.asteroidradar.database.domain

/**
 * Data class that represents a NASA NEO asteroid database model.
 */
data class DatabaseAsteroid(val id: Long, val codename: String, val closeApproachDate: String,
                            val absoluteMagnitude: Double, val estimatedDiameter: Double,
                            val relativeVelocity: Double, val distanceFromEarth: Double,
                            val isPotentiallyHazardous: Boolean)
