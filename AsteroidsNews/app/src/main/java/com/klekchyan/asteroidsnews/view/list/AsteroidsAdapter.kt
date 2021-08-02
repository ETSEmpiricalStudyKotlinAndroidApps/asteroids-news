package com.klekchyan.asteroidsnews.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.klekchyan.asteroidsnews.databinding.AsteroidsListItemBinding
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid

class AsteroidsAdapter(private val listener: AsteroidsAdapterClickListener):
    ListAdapter<SimpleAsteroid, AsteroidsAdapter.AsteroidsViewHolder>(AsteroidsAdapterDiffCallBack()) {

    private val viewBinderHelper = ViewBinderHelper()
    private var shownList: ShownList? = ShownList.ALL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val asteroid = getItem(position)
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.swipeRevealLayout, asteroid.name)
        viewBinderHelper.closeLayout(asteroid.name)
        holder.bind(asteroid, listener, shownList)
    }

    /**
     * The method has 'shownList' parameter to check if the type of list has been changed or not.
     * If the type has been changed, it's needed to refresh viewHolders
     * because they must include different buttons depending on type of list.
     * To refresh viewHolders adapter is given empty list and then given list with content.
     *
     * This way allows to use one adapter for different lists but when user moves between lists
     * position is not saved.
     * */

    fun changeList(list: List<SimpleAsteroid>, shownList: ShownList?){
        if(this.shownList != shownList) {
            this.submitList(listOf<SimpleAsteroid>())
            this.notifyDataSetChanged()
        }
        this.shownList = shownList
        this.submitList(list)
    }

    class AsteroidsViewHolder(val binding: AsteroidsListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        val swipeRevealLayout = binding.swipelayout

        companion object {
            fun from(parent: ViewGroup): AsteroidsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = AsteroidsListItemBinding.inflate(inflater, parent, false)
                return AsteroidsViewHolder(binding)
            }
        }

        fun bind(
            asteroid: SimpleAsteroid,
            listener: AsteroidsAdapterClickListener,
            shownList: ShownList?
        ) {
            binding.asteroid = asteroid
            binding.cliclListener = listener
            when (shownList) {
                ShownList.ALL -> showAddToFavoriteButton()
                else -> showDeleteToFavoriteButton()
            }
            binding.executePendingBindings()
        }

        private fun showAddToFavoriteButton() {
            binding.deleteFromFavoriteIcon.visibility = View.GONE
            binding.addToFavoriteIcon.isVisible = true
        }

        private fun showDeleteToFavoriteButton() {
            binding.addToFavoriteIcon.visibility = View.GONE
            binding.deleteFromFavoriteIcon.isVisible = true
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

class AsteroidsAdapterClickListener(val clickListener: (asteroid: SimpleAsteroid, itemTypeId: Int) -> Unit){
    fun onClick(asteroid: SimpleAsteroid, itemTypeId: Int) = clickListener(asteroid, itemTypeId)
}

