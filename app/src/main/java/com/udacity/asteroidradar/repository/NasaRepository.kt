package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.domain.asDomainModel
import org.json.JSONObject

/**
 * The repository class containing operations for use with NASA APIs.
 */
class NasaRepository(private val asteroidsDatabase: AsteroidsDatabase) {

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

    /**
     * Refreshes the list of asteroids by making an API call to retrieve asteroids based on the dates passed.
     */
    suspend fun refreshAsteroids(startDate: String, endDate: String): List<Asteroid> {

        // Retrieve network asteroids JSON response
        val networkAsteroids = NasaApi.nasaApiService.getAsteroids(
            BuildConfig.API_KEY,
            startDate,
            endDate
        )

        // Parse JSON into our NetworkAsteroidList
        val networkAsteroidList = parseAsteroidsJsonResult(JSONObject(networkAsteroids))

        // Transform them into our domain model
        val asteroids = networkAsteroidList.asDomainModel()
        return asteroids
    }
}