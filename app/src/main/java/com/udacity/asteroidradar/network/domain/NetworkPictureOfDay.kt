package com.udacity.asteroidradar.network.domain

import com.squareup.moshi.Json

/**
 * Data class that represents the NASA Picture of the Day model from the network.
 */
data class NetworkPictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)