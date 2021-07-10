package com.klekchyan.asteroidsnews.view.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.network.AverageSize
import java.util.*

class FilterViewModel: ViewModel() {

    private val _startDate = MutableLiveData<Date>()
    private val _endDate = MutableLiveData<Date>()
    private val _isHazardous = MutableLiveData<Boolean>()
    private val _size = MutableLiveData<AverageSize>()

    val startDate: LiveData<Date>
        get() = _startDate
    val endDate: LiveData<Date>
        get() = _endDate
    val isHazardous: LiveData<Boolean>
        get() = _isHazardous
    val size: LiveData<AverageSize>
        get() = _size
}