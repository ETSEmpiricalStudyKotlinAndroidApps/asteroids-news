package com.klekchyan.asteroidsnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding


class ListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var binding: FragmentListBinding = DataBindingUtil.inflate(inflater,
                                                                    R.layout.fragment_list,
                                                                    container,
                                                                    false)

        binding.testButton.setOnClickListener{
            Toast.makeText(activity, "Button was clicked", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}