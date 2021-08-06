package com.klekchyan.asteroidsnews.view.specificAsteroid

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.databinding.CloseApproachDataHeaderBinding
import com.klekchyan.asteroidsnews.databinding.CloseApproachDataItemBinding
import com.klekchyan.asteroidsnews.domain.CloseApproachData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1

class CloseApproachDataAdapter :
        ListAdapter<DataItem, RecyclerView.ViewHolder>(CloseApproachDataAdapterDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_HEADER -> HeaderViewHolder.from(parent)
            TYPE_ITEM -> CloseApproachDataViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CloseApproachDataViewHolder -> {
                val item = getItem(position) as DataItem.CloseApproachDataItem
                holder.bind(item.closeApproachData) }
            is HeaderViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> TYPE_HEADER
            is DataItem.CloseApproachDataItem -> TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<CloseApproachData>?){
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map{DataItem.CloseApproachDataItem(it)}
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
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

    fun bind(data: CloseApproachData?){
        binding.data = data
    }
}

class HeaderViewHolder(val binding: CloseApproachDataHeaderBinding): RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent: ViewGroup): HeaderViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = CloseApproachDataHeaderBinding.inflate(inflater, parent, false)
            return HeaderViewHolder(binding)
        }
    }

    fun bind(){
        val draw = binding.animatedArrow.drawable
        if(draw is AnimatedVectorDrawable){
            draw.start()
            draw.registerAnimationCallback(object: Animatable2.AnimationCallback(){
                override fun onAnimationEnd(drawable: Drawable?) {
                    draw.start()
                }
            })
        }
    }
}

class CloseApproachDataAdapterDiffCallback: DiffUtil.ItemCallback<DataItem>(){
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.date == newItem.date
    }
}

sealed class DataItem {
    abstract val date: Date?

    data class CloseApproachDataItem(val closeApproachData: CloseApproachData?): DataItem(){
        override val date = closeApproachData?.closeApproachDate
    }

    object Header : DataItem(){
        override val date = Date()
    }
}