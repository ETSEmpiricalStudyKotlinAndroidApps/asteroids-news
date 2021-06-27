package com.klekchyan.asteroidsnews.specificAsteroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.databinding.CloseApproachDataItemBinding
import com.klekchyan.asteroidsnews.domain.CloseApproachData

class CloseApproachDataAdapter:
        ListAdapter<CloseApproachData, CloseApproachDataViewHolder>(CloseApproachDataAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CloseApproachDataViewHolder {
        return CloseApproachDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CloseApproachDataViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}

class CloseApproachDataViewHolder(val binding: CloseApproachDataItemBinding): RecyclerView.ViewHolder(binding.root){

    companion object{
        fun from(parent: ViewGroup): CloseApproachDataViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = CloseApproachDataItemBinding.inflate(inflater, parent, false)
            return CloseApproachDataViewHolder(binding)
        }
    }

    fun bind(data: CloseApproachData){
        binding.data = data
    }
}

class CloseApproachDataAdapterDiffCallback(): DiffUtil.ItemCallback<CloseApproachData>(){
    override fun areItemsTheSame(oldItem: CloseApproachData, newItem: CloseApproachData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CloseApproachData, newItem: CloseApproachData): Boolean {
        return oldItem.closeApproachDate == newItem.closeApproachDate
    }
}