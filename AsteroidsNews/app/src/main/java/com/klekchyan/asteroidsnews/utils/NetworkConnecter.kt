package com.klekchyan.asteroidsnews.utils

import android.net.Uri
import io.reactivex.Observable
import java.net.HttpURLConnection
import java.net.URL

//To receive information of list of asteroids you need to do this request with parameters below
    //LIST OF ASTEROIDS
//GET https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY
//start_date YYYY-MM-DD none
//end_date YYYY-MM-DD 7 days after start_date
//api_key

    //To receive information of one asteroid you need to do this request
    //SPECIFIC ASTEROID
//https://api.nasa.gov/neo/rest/v1/neo/3542519?api_key=DEMO_KEY
//asteroid_id
//api_key

const val NASA_API_BASE_URL = "https://api.nasa.gov/"
const val NASA_ASTEROIDS_NEOWS = "neo/rest/"
const val NASA_ASTEROIDS_VERSION = "v1/"
const val REQUEST_TYPE_FEED = "feed"
const val REQUEST_TYPE_NEO = "neo"
const val PARAMETER_START_DATE = "start_date" //YYYY-MM-DD
const val PARAMETER_END_DATE = "end_date" //YYYY-MM-DD
const val PARAMETER_ASTEROID_ID = "asteroid_id"
const val PARAMETER_API_KEY = "api_key"
const val API_KEY = "iPIGH6W4IyUt01pS3cOtKOgLNYzTlFDoHMgY139m"

fun getObserver(type: RequestType): Observable<String>{
    return Observable.create<String>{
        val url = getUrl(type)
        val response = getResponse(url)
        it.onNext(response)
    }
}

fun getUrl(requestType: RequestType,
           startDate: String = "",
           endDate: String = "",
           asteroidId: String = ""): String{

    var uri: Uri = when(requestType){

        RequestType.ALL_ASTEROIDS -> Uri.parse(NASA_API_BASE_URL +
                                NASA_ASTEROIDS_NEOWS +
                                NASA_ASTEROIDS_VERSION +
                                REQUEST_TYPE_FEED)
            .buildUpon()
            .appendQueryParameter(PARAMETER_START_DATE, startDate)
            .appendQueryParameter(PARAMETER_END_DATE, endDate)
            .appendQueryParameter(PARAMETER_API_KEY, API_KEY)
            .build()

        RequestType.SPECIFIC_ASTEROID -> Uri.parse(NASA_API_BASE_URL +
                NASA_ASTEROIDS_NEOWS +
                NASA_ASTEROIDS_VERSION +
                REQUEST_TYPE_NEO)
            .buildUpon()
            .appendQueryParameter(PARAMETER_ASTEROID_ID, asteroidId)
            .appendQueryParameter(PARAMETER_API_KEY, API_KEY)
            .build()
    }
    return uri.toString()
}

fun getResponse(url: String): String {
    val urlConnection = URL(url).openConnection() as HttpURLConnection

    try{
        urlConnection.connect()

        if(urlConnection.responseCode != HttpURLConnection.HTTP_OK){
            throw RuntimeException(urlConnection.responseMessage)
        }
        else{
            return urlConnection.inputStream.bufferedReader().readText()
        }
    } finally {
        urlConnection.disconnect()
    }
}

enum class RequestType{
    ALL_ASTEROIDS,
    SPECIFIC_ASTEROID
}