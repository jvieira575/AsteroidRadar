package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getDefaultStartDateFormatted
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] designed to store and manage UI-related data in a lifecycle conscious way. Used in
 * [com.udacity.asteroidradar.MainActivity].
 */
enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // The asteroid database and NASA repository
    private val asteroidsDatabase = getDatabase(application)
    private val nasaRepository = NasaRepository(asteroidsDatabase)

    // Live Data to hold the NASA APOD
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // Live Data to hold the Asteroids to display to the user
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    // Live Data to hold whether the user is/has navigated to the details screen
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid


    // The internal MutableLiveData that stores the status of the most recent asteroid request
    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    init {

        // Retrieve the latest picture of the day and asteroids
        getNasaPictureOfTheDay()
        getAsteroids()
    }

    /**
     * Retrieves the NASA APOD.
     */
    private fun getNasaPictureOfTheDay() {
        viewModelScope.launch {
            try {

                // Retrieve the NASA APOD but process images ONLY!
                val pictureOfDay = nasaRepository.getPictureOfTheDay()
                if (pictureOfDay.mediaType == "image") {
                    _pictureOfDay.value = pictureOfDay
                }

            } catch (e: Exception) {
                Timber.e(e, "Could not retrieve the NASA APOD...")
            }
        }
    }

    /**
     * Retrieves NEO (asteroids).
     */
    private fun getAsteroids() {
        viewModelScope.launch {
            try {

                _status.value = AsteroidApiStatus.LOADING
                val asteroids = nasaRepository.refreshAsteroids(getDefaultStartDateFormatted(), getDefaultStartDateFormatted())
                _asteroids.value = asteroids
                _status.value = AsteroidApiStatus.DONE
                Timber.e("Asteroids: %s", asteroids.size)
            } catch (e: Exception) {
                Timber.e(e, "Could not retrieve the NASA NEO...")
                _status.value = AsteroidApiStatus.ERROR
                _asteroids.value = ArrayList()
            }
        }
    }

    fun navigateToSelectedAsteroid(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun navigateToSelectedAsteroidCompleted() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Factory for constructing MainViewModel instances.
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct MainViewModel")
        }
    }
}