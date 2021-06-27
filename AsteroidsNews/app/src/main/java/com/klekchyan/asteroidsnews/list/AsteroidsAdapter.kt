package com.klekchyan.asteroidsnews.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.databinding.AsteroidsListItemBinding
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid

class AsteroidsAdapter(private val listener: AsteroidsAdapterClickListener):
        ListAdapter<SimpleAsteroid, AsteroidsAdapter.AsteroidsViewHolder>(AsteroidsAdapterDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid, listener)
    }

    class AsteroidsViewHolder(val binding: AsteroidsListItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup): AsteroidsViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = AsteroidsListItemBinding.inflate(inflater, parent, false)
                return AsteroidsViewHolder(binding)
            }
        }

        fun bind(asteroid: SimpleAsteroid, listener: AsteroidsAdapterClickListener){
            binding.asteroid = asteroid
            binding.cliclListener = listener
            binding.executePendingBindings()
        }
    }
}

class AsteroidsAdapterDiffCallBack: DiffUtil.ItemCallback<SimpleAsteroid>(){
    override fun areItemsTheSame(oldItem: SimpleAsteroid, newItem: SimpleAsteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SimpleAsteroid, newItem: SimpleAsteroid): Boolean {
        return oldItem == newItem
    }
}

class AsteroidsAdapterClickListener(val clickListener: (asteroid: SimpleAsteroid) -> Unit){
    fun onClick(asteroid: SimpleAsteroid) = clickListener(asteroid)
}

