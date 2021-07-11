package com.klekchyan.asteroidsnews.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.klekchyan.asteroidsnews.network.NetworkExtendedModel
import com.klekchyan.asteroidsnews.network.NetworkSimpleAsteroid
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

const val START_DATE = "start_date="
const val END_DATE = "end_date="
private val dateFormatter = SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.ENGLISH)

fun getListOfSimpleAsteroidsFromResponse(response: String): MutableList<NetworkSimpleAsteroid>{

    val mainObject = JSONObject(response)
    val nearEarthObjects = mainObject.getJSONObject("near_earth_objects")
    val dates = getStartAndEndDates(mainObject)

    return getAllAsteroids(dates, nearEarthObjects)
}

fun getExtendedAsteroidFromResponse(response: String): NetworkExtendedModel{
    val networkAsteroid = Gson().fromJson(response, NetworkExtendedModel::class.java)
    Timber.d("Asteroid was parsed. Id - ${networkAsteroid.id}")
    return networkAsteroid
}

private fun getAllAsteroids(dates: Pair<LocalDate, LocalDate>, nearEarthObjects: JSONObject): MutableList<NetworkSimpleAsteroid>{

    val allAsteroids = mutableListOf<NetworkSimpleAsteroid>()

    var startDate = dates.first
    var endDate = dates.second

    try{
        while(startDate <= endDate){
            val array = nearEarthObjects.getJSONArray(startDate.toString())
            if(array != null && array.length() != 0){
                val length = array.length()
                for(i in 0 until length){
                    val asteroid = Gson().fromJson(array[i].toString(), NetworkSimpleAsteroid::class.java)
                    allAsteroids.add(asteroid)
                }
            }
            startDate = startDate.plusDays(1L)
        }
        Timber.d("parsing was finished")
    } catch (e: JSONException){
        Timber.d(e)
    }
    return allAsteroids
}

private fun getStartAndEndDates(obj: JSONObject): Pair<LocalDate, LocalDate>{
    val links = obj.getJSONObject("links")
    val selfLink = links.get("self").toString()

    val start = selfLink.substringAfter(START_DATE).substringBefore("&")
    val end = selfLink.substringAfter(END_DATE).substringBefore("&")

    var startDate = LocalDate.parse(start)
    val endDate = LocalDate.parse(end)

    return startDate to endDate
}
