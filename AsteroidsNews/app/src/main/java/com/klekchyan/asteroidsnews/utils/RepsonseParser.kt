package com.klekchyan.asteroidsnews.utils

import com.google.gson.Gson
import com.klekchyan.asteroidsnews.network.NetworkExtendedAsteroid
import com.klekchyan.asteroidsnews.network.NetworkSimpleAsteroid
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.*

private const val START_DATE = "start_date="
private const val END_DATE = "end_date="

/**
 * Returns all asteroids as NetworkSimpleAsteroids
 *
 * @param response is a response as string from NasaApi
 * */
fun getListOfSimpleAsteroidsFromResponse(response: String): MutableList<NetworkSimpleAsteroid>{

    val mainObject = JSONObject(response)
    val nearEarthObjects = mainObject.getJSONObject("near_earth_objects")
    val dates = getStartAndEndDates(mainObject)

    return getAllAsteroids(dates, nearEarthObjects)
}

/**
 * Returns asteroid as NetworkExtendedAsteroid
 *
 * @param response is a response as string from NasaApi
 * */
fun getExtendedAsteroidFromResponse(response: String): NetworkExtendedAsteroid{
    val networkAsteroid = Gson().fromJson(response, NetworkExtendedAsteroid::class.java)
    Timber.d("Asteroid was parsed. Id - ${networkAsteroid.id}")
    return networkAsteroid
}

/**
 * Goes through each date of date range and looks for asteroids to add to common list to return its.
 * GSON is used to parse JSON and create NetworkSimpleAsteroid object
 *
 * @param dates date range of response
 * @param nearEarthObjects JSON object containing sets of asteroids belonging to each date
 * */
private fun getAllAsteroids(dates: Pair<Date, Date>, nearEarthObjects: JSONObject): MutableList<NetworkSimpleAsteroid>{

    val allAsteroids = mutableListOf<NetworkSimpleAsteroid>()

    var startDate = dates.first
    val endDate = dates.second

    try{
        while(startDate <= endDate){
            val array = nearEarthObjects.getJSONArray(dateTypeCast(startDate, DateType.DATE_DASH_SEPARATOR))
            if(array.length() != 0){
                val length = array.length()
                for(i in 0 until length){
                    val asteroid = Gson().fromJson(array[i].toString(), NetworkSimpleAsteroid::class.java)
                    allAsteroids.add(asteroid)
                }
            }
            startDate = startDate.addDay(1)
        }
        Timber.d("parsing was finished")
    } catch (e: JSONException){
        Timber.d(e)
    }
    return allAsteroids
}

/**
 * Returns date range
 *
 * @param mainObject JSON object containing link 'self' which contains start and end dates of request
 * */
private fun getStartAndEndDates(mainObject: JSONObject): Pair<Date, Date>{
    val links = mainObject.getJSONObject("links")
    val selfLink = links.get("self").toString()

    val start = selfLink.substringAfter(START_DATE).substringBefore("&")
    val end = selfLink.substringAfter(END_DATE).substringBefore("&")

    val startDate = start.getDateFromNasaApiResponseFormat(DateType.DATE_DASH_SEPARATOR)
    val endDate = end.getDateFromNasaApiResponseFormat(DateType.DATE_DASH_SEPARATOR)
    //2015-09-08

    return startDate to endDate
}
