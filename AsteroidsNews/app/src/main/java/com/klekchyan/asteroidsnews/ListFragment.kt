package com.klekchyan.asteroidsnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.klekchyan.asteroidsnews.databinding.FragmentListBinding
import com.klekchyan.asteroidsnews.utils.RequestType
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import com.klekchyan.asteroidsnews.utils.getObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    var request: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                                            R.layout.fragment_list,
                                            container,
                                            false)

        val observer = getObserver(RequestType.ALL_ASTEROIDS)
            .map { it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        request = observer.subscribe({
            val asteroids = getListOfAsteroidsFromResponse(it)
            var stringBuilder: StringBuilder = StringBuilder()
            for(item in asteroids){
                stringBuilder.append("${item.name} ${item.estimatedDiameter.meters.max} \n")
            }
            binding.listText.text = stringBuilder.toString()
        }, {
            Log.e("Parsing", it.message.toString())
            Toast.makeText(context, "Exception was thrown", Toast.LENGTH_SHORT).show()
            //TODO Catch exceptions
        })

        binding.testButton.setOnClickListener{
            Toast.makeText(activity, "Button was clicked", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        request?.dispose()
    }
}