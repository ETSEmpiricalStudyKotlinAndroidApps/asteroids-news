package com.klekchyan.asteroidsnews.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.domain.CloseApproachData
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid
import com.klekchyan.asteroidsnews.domain.Planet
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.view.specificAsteroid.CloseApproachDataAdapter
import java.util.Date
import kotlin.Pair

//Binding for ListFragment
@BindingAdapter("setAsteroidImageAndDescription")
fun ImageView.setAsteroidImageAndDescription(item: SimpleAsteroid?){
    item?.let {
        val (imageId, descriptionId) = getImageAndDescription(it.isHazardous, it.averageSize)
        setImageResource(imageId)
        contentDescription = context.getString(descriptionId)
    }
}

@BindingAdapter("setDate")
fun TextView.setDate(date: Date?){
    val dateString = dateTypeCast(date ?: Date(), DateType.DATE_AND_TIME)
    text = dateString
}

private fun getImageAndDescription(isHazardous: Boolean, averageSize: AverageSize): Pair<Int, Int>{
    return when{
        isHazardous && (averageSize == AverageSize.SMALL) -> {
            R.drawable.ic_small_dangerous_asteroid to R.string.small_and_dangerous_description }
        isHazardous && (averageSize == AverageSize.MEDIUM) -> {
            R.drawable.ic_medium_dangerous_asteroid to R.string.medium_and_dangerous_description }
        isHazardous && (averageSize == AverageSize.BIG) -> {
            R.drawable.ic_big_dangerous_asteroid to R.string.big_and_dangerous_description }
        !isHazardous && (averageSize == AverageSize.SMALL) -> {
            R.drawable.ic_small_asteroid to R.string.small_and_not_dangerous_description }
        !isHazardous && (averageSize == AverageSize.MEDIUM) -> {
            R.drawable.ic_medium_asteroid to R.string.medium_and_not_dangerous_description }
        else -> R.drawable.ic_big_asteroid to R.string.big_and_not_dangerous_description
    }
}

//Binding for SpecificAsteroidFragment
@BindingAdapter("listOfCloseApproachData")
fun RecyclerView.setListOfData(list: List<CloseApproachData>?){
    val adapter = adapter as CloseApproachDataAdapter
    adapter.addHeaderAndSubmitList(list)
}

@BindingAdapter("setPlanet")
fun ImageView.setPlanet(data: CloseApproachData?){
    data?.let {
        setImageResource(when(data.planet){
            Planet.MERC -> R.drawable.ic_mercury
            Planet.VENUS -> R.drawable.ic_venus
            Planet.EARTH -> R.drawable.ic_earth
            Planet.MARS -> R.drawable.ic_mars
            Planet.JUPTR -> R.drawable.ic_jupiter
            Planet.MOON -> R.drawable.ic_moon
            else -> R.drawable.ic_big_dangerous_asteroid
            //TODO Need to add ic for undefined planet
        })
    }
}

@BindingAdapter("setVelocity")
fun TextView.setVelocity(velocity: Double?){
    val roundValue = "${velocity?.toInt()} km/h"
    text = roundValue
}

@BindingAdapter("setKilometersMissDistance")
fun TextView.setKilometersMissDistance(distance: Double?){
    val roundValue = "${distance?.toInt()} km"
    text = roundValue
}

@BindingAdapter("setAverageDiameter")
fun TextView.setAverageDiameter(asteroid: ExtendedAsteroid?){
    val averageDiameter = "${((asteroid?.minDiameterMeters?.plus(asteroid.maxDiameterMeters))?.div(2))?.toInt()} m"
    text = averageDiameter
}