package com.klekchyan.asteroidsnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.recycler.AsteroidsAdapter
import com.klekchyan.asteroidsnews.utils.RequestType
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import com.klekchyan.asteroidsnews.utils.getObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ListFragment : Fragment(), AsteroidsAdapter.AsteroidsAdapterOnClickHandler {

    lateinit var binding: FragmentListBinding
    lateinit var recyclerView: RecyclerView
    lateinit var asteroidsAdapter: AsteroidsAdapter
    lateinit var layoutManager: LinearLayoutManager
    var request: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                                            R.layout.fragment_list,
                                            container,
                                            false)

        layoutManager = LinearLayoutManager(activity)
        asteroidsAdapter = AsteroidsAdapter(this)
        recyclerView = binding.asteroidsRecyclerView

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = asteroidsAdapter

        getAsteroids()

        return binding.root
    }

    private fun getAsteroids(){
        val observer = getObserver(RequestType.ALL_ASTEROIDS)
            .map { getListOfAsteroidsFromResponse(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        request = observer.subscribe({
            asteroidsAdapter.setListOfAsteroids(it)
        }, {
            Log.e("Parsing", it.message.toString())
            Toast.makeText(context, "Exception was thrown", Toast.LENGTH_SHORT).show()
            //TODO Catch exceptions
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        request?.dispose()
    }

    override fun onClickHandler(asteroid: Asteroid, view: View) {
        view.findNavController().navigate(ListFragmentDirections.actionListFragmentToSpecificAsteroidFragment(asteroid))
    }
}