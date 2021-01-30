package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.worker.AsteroidRefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * The Asteroid Radar [Application] class.
 */
class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * Invoked when the [Application] first starts up.
     */
    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
        Timber.plant(Timber.DebugTree())
    }

    /**
     * Sets up the recurring work to perform.
     */
    private fun setupRecurringWork() {

        applicationScope.launch {
            // Build the constraints
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()

            // Repeat the job once a day
            val repeatingRequest =
                PeriodicWorkRequestBuilder<AsteroidRefreshDataWorker>(1, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .build()

            // Enqueue the work
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                AsteroidRefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }

    }
}