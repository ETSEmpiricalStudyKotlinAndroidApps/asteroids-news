package com.klekchyan.asteroidsnews.view.list

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.view.filter.FilterViewModel
import timber.log.Timber

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val listViewModel: ListViewModel by viewModels()
    private val filterViewModel: FilterViewModel by viewModels({ requireActivity() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(inflater)
        setHasOptionsMenu(true)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AsteroidsAdapter(AsteroidsAdapterClickListener { asteroid ->
            listViewModel.onAsteroidClicked(asteroid)
        })

        val asteroidTouchHelper = ItemTouchHelper(AsteroidTouchHelperCallback(listViewModel, adapter))
        asteroidTouchHelper.attachToRecyclerView(binding?.asteroidsRecyclerView)

        binding?.lifecycleOwner = this
        binding?.viewModel = listViewModel
        binding?.asteroidsRecyclerView?.adapter  = adapter

        listViewModel.navigateToSpecificAsteroid.observe(viewLifecycleOwner, { asteroid ->
            asteroid?.let {
                findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToSpecificAsteroidFragment(it.id))
                listViewModel.onSpecificAsteroidNavigateDone()
            }
        })

        listViewModel.navigateToFilterFragment.observe(viewLifecycleOwner, { isClicked ->
            if (isClicked){
                findNavController().navigate(ListFragmentDirections.actionListFragmentToFilterFragment())
                listViewModel.onFilterNavigateDone()
            }
        })

        listViewModel.shownList.observe(viewLifecycleOwner, { shownList ->
            when(shownList){
                ShownList.ALL -> selectAll()
                ShownList.FAVORITE -> selectFavorite()
            }
        })

        listViewModel.progressIndicatorState.observe(viewLifecycleOwner, { state ->
            if(state){
                binding?.progressIndicator?.isVisible = state
            } else {
                binding?.progressIndicator?.visibility = View.GONE
            }

        })

        filterViewModel.dateRange.observe(viewLifecycleOwner, { newDateRange ->
            listViewModel.changeDateRange(newDateRange)
        })

        filterViewModel.isHazardousFilter.observe(viewLifecycleOwner, { isHazardous ->
            listViewModel.changeFilterByHazardous(isHazardous)
        })

        filterViewModel.averageSizeFilter.observe(viewLifecycleOwner, { range ->
            listViewModel.changeFilterByAverageSize(range)
        })
    }

    private fun selectAll(){
        binding?.allAsteroids?.typeface = Typeface.DEFAULT_BOLD
        binding?.favoriteAsteroids?.typeface = Typeface.DEFAULT
    }

    private fun selectFavorite(){
        binding?.allAsteroids?.typeface = Typeface.DEFAULT
        binding?.favoriteAsteroids?.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.filter_item){
            listViewModel.onFilterClicked()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}