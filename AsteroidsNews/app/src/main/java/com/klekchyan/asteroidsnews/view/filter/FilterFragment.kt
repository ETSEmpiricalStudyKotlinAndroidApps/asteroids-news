package com.klekchyan.asteroidsnews.view.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentFilterBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_filter,
                                                                        container,
                                                                        false)
        return binding.root
    }

}