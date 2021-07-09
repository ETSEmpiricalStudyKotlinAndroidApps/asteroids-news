package com.klekchyan.asteroidsnews.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.databinding.FragmentListBindingImpl

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val viewModel: ListViewModel by viewModels {
        ListViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AsteroidsAdapter(AsteroidsAdapterClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        val asteroidTouchHelper = ItemTouchHelper(AsteroidTouchHelperCallback(viewModel, adapter))
        asteroidTouchHelper.attachToRecyclerView(binding?.asteroidsRecyclerView)

        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.asteroidsRecyclerView?.adapter  = adapter

        viewModel.navigateToSpecificAsteroid.observe(viewLifecycleOwner, { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToSpecificAsteroidFragment(it.id))
                viewModel.onSpecificAsteroidNavigated()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}