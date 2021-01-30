package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getDefaultEndDateFormatted
import com.udacity.asteroidradar.api.getDefaultStartDateFormatted
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import retrofit2.HttpException
import timber.log.Timber

/**
 * A [CoroutineWorker] class that refreshes the database by retrieving and saving today's NASA NEO asteroids to the database.
 */
class AsteroidRefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = NasaRepository(database)

        return try {
            Timber.i("AsteroidRefreshDataWorker refreshing asteroids...")
            repository.refreshAsteroids(
                getDefaultStartDateFormatted(),
                getDefaultEndDateFormatted()
            )
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AsteroidRefreshDataWorker"
    }
}