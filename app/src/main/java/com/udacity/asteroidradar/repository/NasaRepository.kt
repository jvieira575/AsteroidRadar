package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi

/**
 * The repository class containing operations for use with NASA APIs.
 */
class NasaRepository {

    /**
     * Retrieves the NASA APOD (Astronomy Picture of the Day).
     */
    suspend fun getPictureOfTheDay(): PictureOfDay {
        val networkPictureOfDay = NasaApi.nasaApiService.getNetworkPictureOfDay()
        return PictureOfDay(
            networkPictureOfDay.mediaType,
            networkPictureOfDay.title,
            networkPictureOfDay.url
        )
    }
}