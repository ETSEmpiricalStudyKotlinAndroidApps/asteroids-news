package com.klekchyan.asteroidsnews.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val NASA_API_BASE_URL = "https://api.nasa.gov/"
private const val NASA_ASTEROIDS_NEOWS = "neo/rest/"
private const val NASA_ASTEROIDS_VERSION = "v1/"

private const val PARAMETER_START_DATE = "start_date" //YYYY-MM-DD
private const val PARAMETER_END_DATE = "end_date" //YYYY-MM-DD
private const val PARAMETER_ASTEROID_ID = "asteroid_id"
private const val PARAMETER_API_KEY = "api_key"

private const val API_KEY = "iPIGH6W4IyUt01pS3cOtKOgLNYzTlFDoHMgY139m"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(NASA_API_BASE_URL + NASA_ASTEROIDS_NEOWS + NASA_ASTEROIDS_VERSION)
    .build()

interface NasaApiService {
    @GET("feed")
    fun getAllAsteroids(
        @Query(PARAMETER_START_DATE) startDay: String = "",
        @Query(PARAMETER_END_DATE) endDay: String = "",
        @Query(PARAMETER_API_KEY) apiKey: String = API_KEY
    ) : Deferred<String>

    @GET("neo")
    fun getSpecificAsteroid(
        @Query(PARAMETER_ASTEROID_ID) asteroidId: String,
        @Query(PARAMETER_API_KEY) apiKey: String = API_KEY
    ) : Deferred<String>
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}