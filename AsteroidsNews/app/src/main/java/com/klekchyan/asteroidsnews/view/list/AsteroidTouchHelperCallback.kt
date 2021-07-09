package com.klekchyan.asteroidsnews.view.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.domain.SimpleAsteroid

interface AsteroidTouchHelperAdapter{
    fun onItemSwiped(position: Int): SimpleAsteroid
}

class AsteroidTouchHelperCallback(
        val viewModel: ListViewModel,
        val adapter: AsteroidsAdapter): ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val asteroid = adapter.onItemSwiped(viewHolder.absoluteAdapterPosition)
        viewModel.onAsteroidSwiped(asteroid)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        //TODO()
        return super.isItemViewSwipeEnabled()
    }
}