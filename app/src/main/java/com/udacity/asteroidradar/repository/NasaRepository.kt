package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.getDefaultEndDateFormatted
import com.udacity.asteroidradar.api.getDefaultStartDateFormatted
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.domain.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.domain.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    suspend fun refreshAsteroids(startDate: String, endDate: String) {

        withContext(Dispatchers.IO) {
            // Retrieve network asteroids JSON response
            val networkAsteroids = NasaApi.nasaApiService.getAsteroids(
                BuildConfig.API_KEY,
                startDate,
                endDate
            )

            // Parse JSON into our NetworkAsteroidList
            val networkAsteroidList = parseAsteroidsJsonResult(JSONObject(networkAsteroids))
            asteroidsDatabase.asteroidDao.insertAll(*networkAsteroidList.asDatabaseModel())
        }
    }

    /**
     * Retrieves a list of asteroids from the database using today's date.
     */
    fun getAsteroidsForToday(): LiveData<List<Asteroid>> {

        return Transformations.map(
            asteroidsDatabase.asteroidDao.getAsteroidsByDate(
                getDefaultStartDateFormatted(), getDefaultStartDateFormatted()
            )
        ) {
            it.asDomainModel()
        }
    }

    /**
     * Retrieves a list of asteroids from the database for the week.
     */
    fun getAsteroidsForWeek(): LiveData<List<Asteroid>> {
        return Transformations.map(
            asteroidsDatabase.asteroidDao.getAsteroidsByDate(
                getDefaultStartDateFormatted(), getDefaultEndDateFormatted()
            )
        ) {
            it.asDomainModel()
        }
    }

    /**
     * Retrieves a list of all asteroids in the database.
     */
    fun getAllAsteroids(): LiveData<List<Asteroid>> {
        return Transformations.map(
            asteroidsDatabase.asteroidDao.getAllAsteroids()
        ) {
            it.asDomainModel()
        }
    }
}