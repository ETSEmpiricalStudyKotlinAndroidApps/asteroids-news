package com.klekchyan.asteroidsnews.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.domain.CloseApproachData
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid
import com.klekchyan.asteroidsnews.list.AsteroidsAdapter
import com.klekchyan.asteroidsnews.list.NasaApiStatus
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.specificAsteroid.CloseApproachDataAdapter


//Binding for ListFragment
@BindingAdapter("setAsteroidImage")
fun ImageView.setAsteroidImage(item: SimpleAsteroid?){
    item?.let {
        setImageResource(getImage(it.isHazardous, it.averageSize))
    }
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
    val adapter = adapter as AsteroidsAdapter
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
fun RecyclerView.setListOfData(data: List<CloseApproachData>?){
    val adapter = adapter as CloseApproachDataAdapter
    adapter.submitList(data)
}