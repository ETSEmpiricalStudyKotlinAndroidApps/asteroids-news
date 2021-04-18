package com.klekchyan.asteroidsnews.utils

import com.google.gson.Gson
import com.klekchyan.asteroidsnews.model.Asteroid
import org.json.JSONObject
import java.time.LocalDate

const val START_DATE = "start_date="
const val END_DATE = "end_date="

fun getListOfAsteroidsFromResponse(response: String): MutableList<Asteroid>{

    val mainObject = JSONObject(response)
    val nearEarthObjects = mainObject.getJSONObject("near_earth_objects")
    val dates = getStartAndEndDates(mainObject)

    return getAsteroids(dates, nearEarthObjects)
}

private fun getAsteroids(dates: Pair<LocalDate, LocalDate>, nearEarthObjects: JSONObject): MutableList<Asteroid>{

    val allAsteroids = mutableListOf<Asteroid>()

    var startDate = dates.first
    var endDate = dates.second

    while(startDate <= endDate){
        val array = nearEarthObjects.getJSONArray(startDate.toString())
        val length = array.length()
        for(i in 0 until length){
            val asteroid = Gson().fromJson(array[i].toString(), Asteroid::class.java)
            allAsteroids.add(asteroid)
        }
        startDate = startDate.plusDays(1L)
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