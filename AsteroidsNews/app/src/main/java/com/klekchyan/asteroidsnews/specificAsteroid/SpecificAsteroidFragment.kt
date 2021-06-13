package com.klekchyan.asteroidsnews.specificAsteroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentSpecificAsteroidBinding


class SpecificAsteroidFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpecificAsteroidBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_specific_asteroid,
                container,
                false)
        val arguments = SpecificAsteroidFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = SpecificAsteroidViewModelFactory(arguments.asteroid)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SpecificAsteroidViewModel::class.java)

        binding.asteroidViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}