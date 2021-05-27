package com.klekchyan.asteroidsnews.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.databinding.AsteroidsListItemBinding
import com.klekchyan.asteroidsnews.model.Asteroid

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

    class AsteroidsViewHolder(val binding: AsteroidsListItemBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        companion object{
            fun from(parent: ViewGroup): AsteroidsViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = AsteroidsListItemBinding.inflate(inflater, parent, false)
                return AsteroidsViewHolder(binding)
            }
        }

        fun bind(asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.executePendingBindings()
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

