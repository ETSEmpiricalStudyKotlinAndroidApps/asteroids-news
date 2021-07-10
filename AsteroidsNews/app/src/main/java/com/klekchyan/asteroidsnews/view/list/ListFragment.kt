package com.klekchyan.asteroidsnews.view.list

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.klekchyan.asteroidsnews.R
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val viewModel: ListViewModel by viewModels {
        ListViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater)
        setHasOptionsMenu(true)

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
                findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToSpecificAsteroidFragment(it.id))
                viewModel.onSpecificAsteroidNavigated()
            }
        })

        viewModel.shownList.observe(viewLifecycleOwner, { shownList ->
            when(shownList){
                ShownList.ALL -> {
                    binding?.allAsteroids?.typeface = Typeface.DEFAULT_BOLD
                    binding?.favoriteAsteroids?.typeface = Typeface.DEFAULT
                }
                ShownList.FAVORITE -> {
                    binding?.allAsteroids?.typeface = Typeface.DEFAULT
                    binding?.favoriteAsteroids?.typeface = Typeface.DEFAULT_BOLD
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.filter_item){
            findNavController().navigate(ListFragmentDirections.actionListFragmentToFilterFragment())
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}