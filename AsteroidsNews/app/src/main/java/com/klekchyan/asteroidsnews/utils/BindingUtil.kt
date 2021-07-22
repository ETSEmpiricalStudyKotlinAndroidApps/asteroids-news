package com.klekchyan.asteroidsnews.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.Pair
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.domain.CloseApproachData
import com.klekchyan.asteroidsnews.domain.ExtendedAsteroid
import com.klekchyan.asteroidsnews.domain.Planet
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.view.list.AsteroidsAdapter
import com.klekchyan.asteroidsnews.view.list.NasaApiStatus
import com.klekchyan.asteroidsnews.view.specificAsteroid.CloseApproachDataAdapter
import java.text.SimpleDateFormat
import java.util.*

//Binding for ListFragment
@BindingAdapter("setAsteroidImage")
fun ImageView.setAsteroidImage(item: SimpleAsteroid?){
    item?.let {
        setImageResource(getImage(it.isHazardous, it.averageSize))
    }
}

@BindingAdapter("setDate")
fun TextView.setDate(date: Date){
    val dateString = dateTypeCast(date, DateType.DATE_AND_TIME)
    text = dateString
}

@BindingAdapter("setStatus")
fun ImageView.setStatus(status: NasaApiStatus?){
    when(status){
        NasaApiStatus.LOADING -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.loading_animation)
        }
        NasaApiStatus.DONE -> visibility = View.GONE
        else -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_connection_error)
        }
    }
}

@BindingAdapter("listOfAsteroids")
fun RecyclerView.setListOfAsteroids(asteroids: List<SimpleAsteroid>?){
    val adapter = this.adapter as AsteroidsAdapter
    adapter.submitList(asteroids)
}

private fun getImage(isHazardous: Boolean, averageSize: AverageSize): Int{
    return when{
        isHazardous && (averageSize == AverageSize.SMALL) -> R.drawable.ic_small_dangerous_asteroid
        isHazardous && (averageSize == AverageSize.MEDIUM) -> R.drawable.ic_medium_dangerous_asteroid
        isHazardous && (averageSize == AverageSize.BIG) -> R.drawable.ic_big_dangerous_asteroid
        !isHazardous && (averageSize == AverageSize.SMALL) -> R.drawable.ic_small_asteroid
        !isHazardous && (averageSize == AverageSize.MEDIUM) -> R.drawable.ic_medium_asteroid
        else -> R.drawable.ic_big_asteroid
    }
}

//Binding for SpecificAsteroidFragment
@BindingAdapter("listOfCloseApproachData")
fun RecyclerView.setListOfData(asteroid: ExtendedAsteroid?){
    val adapter = adapter as CloseApproachDataAdapter
    adapter.addHeaderAndSubmitList(asteroid)
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
            else -> R.drawable.ic_big_dangerous_asteroid
            //TODO Need to add ic for undefined planet
        })
    }
}

@BindingAdapter("setAstronomicalMissDistance")
fun TextView.setAstronomicalMissDistance(distance: Double?){
    val data = context.getString(R.string.astronomical_miss_distance) +  "\n" + distance.toString()
    text = data
}

//Binding for FilterFragment
@BindingAdapter("setDateRange")
fun TextView.setDateRange(range: Pair<Long, Long>){
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val startDate = dateFormat.format(range.first)
    val endDate = dateFormat.format(range.second)
    val string = "$startDate - $endDate"
    text = string
}