package com.klekchyan.asteroidsnews.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.model.AverageSize
import java.math.RoundingMode
import java.text.DecimalFormat

class AsteroidsAdapter(val listener: AsteroidsAdapterOnClickHandler): RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>() {

    interface AsteroidsAdapterOnClickHandler{
        fun onClickHandler(asteroid: Asteroid, view: View)
    }

    private var asteroids: MutableList<Asteroid> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {

        val context = parent.context
        val layoutForItem = R.layout.asteroids_list_item
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutForItem, parent, false)

        return AsteroidsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val asteroid: Asteroid = asteroids[position]

        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING

        val sizeMeters = "${df.format(asteroid.estimatedDiameter.meters.min)} - ${df.format(asteroid.estimatedDiameter.meters.max)} (m)"

        holder.asteroidTextView.text = asteroid.name
        holder.sizeMetersText.text = sizeMeters
        holder.hazardousText.text = asteroid.isHazardous.toString()
        holder.asteroidImage.setImageResource(
                getImage(asteroid.isHazardous,
                getAverageSize(asteroid.estimatedDiameter.meters.min, asteroid.estimatedDiameter.meters.max)))
    }

    private fun getAverageSize(min: Double, max: Double): AverageSize{
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


    override fun getItemCount(): Int {
        return if(asteroids.isEmpty()) 0 else asteroids.size - 1
    }

    fun setListOfAsteroids(asteroids: MutableList<Asteroid>){
        this.asteroids = asteroids
        notifyDataSetChanged()
    }

    inner class AsteroidsViewHolder(itemView: View,
                              val asteroidTextView: TextView = itemView.findViewById(R.id.asteroidsNameText),
                              //val sizeKilometersText: TextView = itemView.findViewById(R.id.sizeKilometers),
                              val sizeMetersText: TextView = itemView.findViewById(R.id.sizeMeters),
                              val hazardousText: TextView = itemView.findViewById(R.id.isHazardous),
                              val asteroidImage: ImageView = itemView.findViewById(R.id.asteroidImage)
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val asteroid = asteroids[bindingAdapterPosition]
            listener.onClickHandler(asteroid, v)
        }
    }
}

