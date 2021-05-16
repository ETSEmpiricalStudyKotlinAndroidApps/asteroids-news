package com.klekchyan.asteroidsnews.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.model.Asteroid

class ListFragment : Fragment(), AsteroidsAdapter.AsteroidsAdapterOnClickHandler {

    private lateinit var binding: FragmentListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var asteroidsAdapter: AsteroidsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: ListViewModel
    private lateinit var menuSwitch: SwitchCompat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_list,
                container,
                false)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        layoutManager = LinearLayoutManager(activity)
        asteroidsAdapter = AsteroidsAdapter(this)
        recyclerView = binding.asteroidsRecyclerView

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = asteroidsAdapter

        viewModel.listOfAsteroids.observe(viewLifecycleOwner, { newAsteroids ->
            asteroidsAdapter.setListOfAsteroids(newAsteroids)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClickHandler(asteroid: Asteroid, view: View) {
        view.findNavController().navigate(ListFragmentDirections.actionListFragmentToSpecificAsteroidFragment(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val item = menu.findItem(R.id.is_hazardous_switch_item)
        item.setActionView(R.layout.is_hazardous_switch_layout)

        menuSwitch = item.actionView.findViewById(R.id.is_hazardous_switch_id)
        menuSwitch.setOnCheckedChangeListener { p0, isChecked ->
            viewModel.getFilteredAsteroids(isChecked)
        }

        viewModel.isHazardous.observe(viewLifecycleOwner, {
            menuSwitch.isChecked = viewModel.isHazardous.value!!
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.reload_menu_item) {
            viewModel.getAllAsteroids()
            menuSwitch.isChecked = false
        }
        return super.onOptionsItemSelected(item)
    }
}