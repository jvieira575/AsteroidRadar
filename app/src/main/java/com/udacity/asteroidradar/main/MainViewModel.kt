package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] designed to store and manage UI-related data in a lifecycle conscious way. Used in
 * [com.udacity.asteroidradar.MainActivity].
 */
class MainViewModel : ViewModel() {

    // The NASA repository
    private val nasaRepository = NasaRepository()

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

                // Create test asteroids
                val asteroidOne = Asteroid(1, "89355 (2001 VS78)", "2021-01-28", 5.00, 10.00, 15.00, 20.00, false )
                val asteroidTwo = Asteroid(1, "337084 (1998 SE36)", "2021-01-28", 25.00, 30.00, 35.00, 40.00, true )
                val testAsteroids = ArrayList<Asteroid>()
                testAsteroids.add(asteroidOne)
                testAsteroids.add(asteroidTwo)

                _asteroids.value = testAsteroids
                Timber.e("Asteroids: %s", asteroids.value?.size)
            } catch (e: Exception) {
                Timber.e(e, "Could not retrieve the NASA NEO...")
            }
        }
    }

    fun navigateToSelectedAsteroid(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun navigateToSelectedAsteroidCompleted() {
        _navigateToSelectedAsteroid.value = null
    }
}