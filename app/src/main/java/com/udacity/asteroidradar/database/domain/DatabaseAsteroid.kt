package com.udacity.asteroidradar.database.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents a NASA NEO asteroid database model.
 */
@Entity
data class DatabaseAsteroid(
    @PrimaryKey val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)
