package com.udacity.asteroidradar.domain

import com.squareup.moshi.Json

/**
 * Data class to hold the NASA Picture of the Day.
 */
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String, val url: String)