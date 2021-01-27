package com.udacity.asteroidradar

import android.app.Application
import timber.log.Timber

/**
 * The Asteroid Radar [Application] class.
 */
class AsteroidRadarApplication : Application() {

    /**
     * Invoked when the [Application] first starts up.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}