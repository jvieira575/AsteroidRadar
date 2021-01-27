package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch

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

    init {

        // Retrieve the latest picture of the day
        getNasaPictureOfTheDay()
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

                // TODO: Configure Timber instead
                Log.e(MainViewModel::class.simpleName, "Could not retrieve the NASA APOD...", e)
            }
        }
    }
}