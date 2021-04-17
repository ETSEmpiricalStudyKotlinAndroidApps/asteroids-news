package com.klekchyan.asteroidsnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.klekchyan.asteroidsnews.databinding.FragmentSpecificAsteroidBinding


class SpecificAsteroidFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var binding: FragmentSpecificAsteroidBinding = DataBindingUtil.inflate(inflater,
                                                                                R.layout.fragment_specific_asteroid,
                                                                                container,
                                                                                false)
        return binding.root
    }
}