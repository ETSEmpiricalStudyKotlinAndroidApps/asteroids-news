package com.klekchyan.asteroidsnews.list

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.model.AverageSize
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("setBackground")
fun ConstraintLayout.setBackground(position: Int?){
    position?.let {
        val darkSnippet = ResourcesCompat.getDrawable(context.resources, R.drawable.dark_snippet, null)
        val lightSnippet = ResourcesCompat.getDrawable(context.resources, R.drawable.light_snippet, null)
        background = if (position % 2 == 0) darkSnippet else lightSnippet
    }
}

@BindingAdapter("setAsteroidNameText")
fun TextView.setAsteroidNameText(item: Asteroid?){
    item?.let {
        text = item.name
    }
}

@BindingAdapter("setApproachDate")
fun TextView.setApproachDate(item: Asteroid?){
    item?.let {
        text = it.closeApproachData[0].date.toString()
    }
}

@BindingAdapter("setSizeMeters")
fun TextView.setSizeMeters(item: Asteroid?){
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    item?.let {
        val sizeMeters = "${df.format(it.estimatedDiameter.meters.min)} - ${df.format(it.estimatedDiameter.meters.max)}"
        text = sizeMeters
    }
}

@BindingAdapter("setHazardous")
fun TextView.setHazardous(item: Asteroid?){
    item?.let {
        text = it.isHazardous.toString()
    }
}

@BindingAdapter("setAsteroidImage")
fun ImageView.setAsteroidImage(item: Asteroid?){
    item?.let {
        setImageResource(getImage(it.isHazardous,
                getAverageSize(it.estimatedDiameter.meters.min, it.estimatedDiameter.meters.max)))
    }
}

private fun getAverageSize(min: Double, max: Double): AverageSize {
    return when((min + max) / 2){
        in 0.0..100.0 -> AverageSize.SMALL
        in 100.1..500.0 -> AverageSize.MEDIUM
        else -> AverageSize.BIG
    }
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