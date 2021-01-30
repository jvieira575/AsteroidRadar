package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getDefaultEndDateFormatted
import com.udacity.asteroidradar.api.getDefaultStartDateFormatted
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch
import timber.log.Timber

// Enum used to display progress bar information when loading asteroids from the network
enum class AsteroidApiStatus { LOADING, ERROR, DONE }

// Enum used to filter date ranges for asteroid database lookups from the main view
enum class AsteroidFilter { SHOW_WEEK, SHOW_TODAY, SHOW_ALL }

/**
 * [ViewModel] designed to store and manage UI-related data in a lifecycle conscious way. Used in
 * [com.udacity.asteroidradar.MainActivity].
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // The asteroid database and NASA repository
    private val asteroidsDatabase = getDatabase(application)
    private val nasaRepository = NasaRepository(asteroidsDatabase)

    // Live Data to hold the NASA APOD
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

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
        getAsteroids()
        getNasaPictureOfTheDay()
    }

    //var asteroids = nasaRepository.getAsteroidsForToday()
    private val _asteroidFilter = MutableLiveData(AsteroidFilter.SHOW_WEEK)
    private val asteroidFilter: LiveData<AsteroidFilter>
        get() = _asteroidFilter

    val asteroids = Transformations.switchMap(asteroidFilter) {
        when (it!!) {
            AsteroidFilter.SHOW_TODAY -> nasaRepository.getAsteroidsForToday()
            AsteroidFilter.SHOW_WEEK -> nasaRepository.getAsteroidsForWeek()
            AsteroidFilter.SHOW_ALL -> nasaRepository.getAllAsteroids()
        }
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
                nasaRepository.refreshAsteroids(
                    getDefaultStartDateFormatted(),
                    getDefaultEndDateFormatted()
                )
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                Timber.e(e, "Could not retrieve the NASA NEO (asteroids)...")
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    /**
     * Sets the Asteroid to pass to the detail screen.
     */
    fun navigateToSelectedAsteroid(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    /**
     * Clears the selected asteroid.
     */
    fun navigateToSelectedAsteroidCompleted() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Filters used to retrieve asteroids.
     */
    fun updateFilter(filter: AsteroidFilter) {
        _asteroidFilter.value = filter
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