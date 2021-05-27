package com.klekchyan.asteroidsnews.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.model.AverageSize
import java.math.RoundingMode
import java.text.DecimalFormat

class AsteroidsAdapter(val listener: AsteroidsAdapterOnClickHandler):
        ListAdapter<Asteroid, AsteroidsAdapter.AsteroidsViewHolder>(AsteroidsAdapterDiffCallBack()) {

    interface AsteroidsAdapterOnClickHandler{
        fun onClickHandler(asteroid: Asteroid, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
    }

    class AsteroidsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        companion object{
            fun from(parent: ViewGroup): AsteroidsViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.asteroids_list_item, parent, false)

                return AsteroidsViewHolder(view)
            }
        }

        val asteroidTextView: TextView = itemView.findViewById(R.id.asteroidsNameText)
        val sizeMetersText: TextView = itemView.findViewById(R.id.sizeMeters)
        val hazardousText: TextView = itemView.findViewById(R.id.isHazardous)
        val asteroidImage: ImageView = itemView.findViewById(R.id.asteroidImage)

        fun bind(asteroid: Asteroid){
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING

            val sizeMeters = "${df.format(asteroid.estimatedDiameter.meters.min)} - ${df.format(asteroid.estimatedDiameter.meters.max)} (m)"

            asteroidTextView.text = asteroid.name
            sizeMetersText.text = sizeMeters
            hazardousText.text = asteroid.isHazardous.toString()
            asteroidImage.setImageResource(
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

        override fun onClick(v: View) {
//            val asteroid = asteroids[bindingAdapterPosition]
//            listener.onClickHandler(asteroid, v)
        }
    }
}

class AsteroidsAdapterDiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

