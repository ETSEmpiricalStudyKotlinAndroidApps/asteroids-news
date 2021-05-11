package com.klekchyan.asteroidsnews.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.model.Asteroid
import com.klekchyan.asteroidsnews.utils.RequestType
import com.klekchyan.asteroidsnews.utils.getListOfAsteroidsFromResponse
import com.klekchyan.asteroidsnews.utils.getObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {
    private val _listOfAsteroids = MutableLiveData<MutableList<Asteroid>>()
    val listOfAsteroids: LiveData<MutableList<Asteroid>>
        get() = _listOfAsteroids

    init {
        getAsteroids()
    }

    @SuppressLint("CheckResult")
    fun getAsteroids(){
        val observer = getObserver(RequestType.ALL_ASTEROIDS)
                .map { getListOfAsteroidsFromResponse(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        observer.subscribe({ asteroids ->
           _listOfAsteroids.value = asteroids
        }, {
            Log.e("Parsing", it.message.toString())
        })
    }
}