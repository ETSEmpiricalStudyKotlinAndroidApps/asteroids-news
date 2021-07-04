package com.klekchyan.asteroidsnews.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireActivity().application
        val binding = FragmentListBinding.inflate(inflater)
        val viewModelFactory = ListViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
        val adapter = AsteroidsAdapter(AsteroidsAdapterClickListener {
            viewModel.onAsteroidClicked(it)
        })

        val asteroidTouchHelper = ItemTouchHelper(AsteroidTouchHelperCallback(viewModel, adapter))
        asteroidTouchHelper.attachToRecyclerView(binding.asteroidsRecyclerView)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.asteroidsRecyclerView.adapter  = adapter

        viewModel.navigateToSpecificAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToSpecificAsteroidFragment(asteroid.id))
                viewModel.onSpecificAsteroidNavigated()
            }
        })

        viewModel.shownList.observe(viewLifecycleOwner, { shownList ->
            when(shownList){
                ShownList.ALL -> {
                    binding.allAsteroids.textSize =
                        resources.getDimension(R.dimen.highlighted_text_size)
                    binding.favoriteAsteroids.textSize =
                        resources.getDimension(R.dimen.not_highlighted_text_size)
                }
                ShownList.FAVORITE -> {
                    binding.favoriteAsteroids.textSize =
                        resources.getDimension(R.dimen.highlighted_text_size)
                    binding.allAsteroids.textSize =
                        resources.getDimension(R.dimen.not_highlighted_text_size)
                }
            }
        })

        return binding.root
    }
}