package com.klekchyan.asteroidsnews.view.specificAsteroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.klekchyan.asteroidsnews.databinding.FragmentSpecificAsteroidBinding


class SpecificAsteroidFragment : Fragment() {

    private var binding: FragmentSpecificAsteroidBinding? = null
    private val viewModel: SpecificAsteroidViewModel by viewModels {
        SpecificAsteroidViewModelFactory(
            SpecificAsteroidFragmentArgs.fromBundle(requireArguments()).asteroidId,
            requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSpecificAsteroidBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        binding?.closeApproachDataRecyclerView?.adapter = CloseApproachDataAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}