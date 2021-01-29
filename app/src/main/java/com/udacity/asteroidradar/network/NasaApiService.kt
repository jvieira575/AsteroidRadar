package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.network.domain.NetworkPictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Service used for network operations with the NASA API(s).
 */
interface NasaApiService {

    @GET("planetary/apod")
    suspend fun getNetworkPictureOfDay(@Query("api_key") apiKey: String = BuildConfig.API_KEY): NetworkPictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apiKey: String = BuildConfig.API_KEY, @Query("start_date") startDate: String, @Query("end_date") endDate: String): String
}

private val nasaApiConverterFactory = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val nasaApiServiceRetrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(nasaApiConverterFactory))
    .baseUrl(BASE_URL)
    .build()

object NasaApi {
    val nasaApiService: NasaApiService by lazy {
        nasaApiServiceRetrofit.create(NasaApiService::class.java)
    }
}