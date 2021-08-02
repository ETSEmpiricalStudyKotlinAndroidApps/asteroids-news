package com.klekchyan.asteroidsnews.view.list

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.repository.DownloadingState
import com.klekchyan.asteroidsnews.view.filter.FilterViewModel

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val listViewModel: ListViewModel by viewModels()
    private val filterViewModel: FilterViewModel by viewModels({ requireActivity() })

    private var isFavoriteList = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(inflater)
        setHasOptionsMenu(true)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AsteroidsAdapter(AsteroidsAdapterClickListener { asteroid, id ->
            when(id){
                0 -> listViewModel.onAsteroidClicked(asteroid)
                1 -> {
                    listViewModel.onAddAsteroidToFavoriteClicked(asteroid)
                    Toast.makeText(context, "Asteroid was added to favorite", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    listViewModel.onDeleteAsteroidFromFavoriteClicked(asteroid)
                    Toast.makeText(context, "Asteroid was deleted from favorite", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding?.lifecycleOwner = this
        binding?.viewModel = listViewModel
        binding?.asteroidsRecyclerView?.adapter  = adapter
        binding?.floatingActionButton?.setOnClickListener {
            listViewModel.onFilterClicked()
        }

        //ListViewModel observation
        listViewModel.listOfAsteroids.observe(viewLifecycleOwner, { list ->
            adapter.changeList(list, isFavoriteList)
        })

        listViewModel.isEmptyList.observe(viewLifecycleOwner, { isEmpty ->
            if(isEmpty) showEmptyListView() else showNotEmptyListView()
        })

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

        listViewModel.navigateToInfoFragment.observe(viewLifecycleOwner, { isClicked ->
            if (isClicked){
                findNavController().navigate(ListFragmentDirections.actionListFragmentToInfoFragment())
                listViewModel.onInfoNavigateDone()
            }
        })

        listViewModel.shownList.observe(viewLifecycleOwner, { shownList ->
            isFavoriteList = when(shownList){
                ShownList.ALL -> {
                    selectAll()
                    false
                }
                ShownList.FAVORITE -> {
                    selectFavorite()
                    true
                }
                else -> throw ClassCastException("Unknown type")
            }
        })

        listViewModel.progressIndicatorState.observe(viewLifecycleOwner, { state ->
            when(state){
                DownloadingState.START -> { binding?.progressIndicator?.isVisible = true }
                DownloadingState.FINISH -> { binding?.progressIndicator?.visibility = View.GONE }
                DownloadingState.FAILURE -> showSnackBar()
                else -> throw ClassCastException("Unknown type")
            }
        })

        //FilterViewModel observation
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

    private fun showEmptyListView(){
        binding?.emptyListText?.isVisible = true
        binding?.asteroidsRecyclerView?.visibility = View.GONE
    }

    private fun showNotEmptyListView(){
        binding?.emptyListText?.visibility = View.GONE
        binding?.asteroidsRecyclerView?.isVisible = true
    }

    private fun showSnackBar(){
        Snackbar.make(binding?.floatingActionButton!!,
            R.string.disconnected,
            Snackbar.LENGTH_LONG)
            .setAnchorView(binding?.floatingActionButton!!)
            .setAction(R.string.try_again){
                listViewModel.startRefreshing()
            }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.info_item){
            listViewModel.onInfoClicked()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}