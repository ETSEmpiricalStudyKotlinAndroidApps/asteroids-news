package com.klekchyan.asteroidsnews.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.klekchyan.asteroidsnews.BuildConfig
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val NASA_API_BASE_URL = "https://api.nasa.gov/"
private const val NASA_ASTEROIDS_NEOWS = "neo/rest/"
private const val NASA_ASTEROIDS_VERSION = "v1/"

private const val PARAMETER_START_DATE = "start_date" //YYYY-MM-DD
private const val PARAMETER_END_DATE = "end_date" //YYYY-MM-DD
private const val PARAMETER_ASTEROID_ID = "asteroid_id" //asteroid_id
private const val PARAMETER_API_KEY = "api_key"

//TODO Need to deal with the default value if local.properties doesn't contain api_key property
private const val API_KEY = BuildConfig.API_KEY

private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

/**
 * It's needed to use ScalarsConverterFactory because NasaApi sends JSON with undefined
 * items count with undefined names, so to parse response @see utils.ResponseParser.kt is used.
 * */
private val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(NASA_API_BASE_URL + NASA_ASTEROIDS_NEOWS + NASA_ASTEROIDS_VERSION)
        .build()

interface NasaApiService {
    @GET("feed")
    fun getAllAsteroidsAsync(
        @Query(PARAMETER_START_DATE) startDay: String = "",
        @Query(PARAMETER_END_DATE) endDay: String = "",
        @Query(PARAMETER_API_KEY) apiKey: String = API_KEY
    ) : Deferred<String>

    @GET("neo/{asteroid_id}")
    fun getSpecificAsteroidAsync(
            @Path(PARAMETER_ASTEROID_ID) asteroidId: Int,
            @Query(PARAMETER_API_KEY) apiKey: String = API_KEY
    ) : Deferred<String>
}

object NasaApi {
    val nasaApiService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}