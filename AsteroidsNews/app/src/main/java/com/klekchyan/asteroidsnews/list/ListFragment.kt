package com.klekchyan.asteroidsnews.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireActivity().application
        val binding = FragmentListBinding.inflate(inflater)
        val viewModelFactory = ListViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        binding.asteroidsRecyclerView.adapter = AsteroidsAdapter(AsteroidsAdapterClickListener {
            viewModel.onAsteroidClicked(it)
        })

        viewModel.navigateToSpecificAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToSpecificAsteroidFragment(asteroid.id))
                viewModel.onSpecificAsteroidNavigated()
            }
        })

        return binding.root
    }
}