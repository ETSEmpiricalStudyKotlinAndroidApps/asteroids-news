package com.klekchyan.asteroidsnews.view.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.core.util.Pair
import com.klekchyan.asteroidsnews.network.AverageSize
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FilterViewModel: ViewModel() {

    private val _dateRange = MutableLiveData<Pair<Long, Long>>()

    private val _isHazardousFilter = MutableLiveData<Boolean>()
    private val _orbitingBodyFilter = MutableLiveData<String>()
    private val _averageSizeFilter = MutableLiveData<Pair<AverageSize, AverageSize>>()

    private val _openDatePicker = MutableLiveData(false)


    val dateRange: LiveData<Pair<Long, Long>>
        get() = _dateRange
    val isHazardousFilter: LiveData<Boolean>
        get() = _isHazardousFilter
    val orbitingBodyFilter: LiveData<String>
        get() = _orbitingBodyFilter
    val averageSizeFilter: LiveData<Pair<AverageSize, AverageSize>>
        get() = _averageSizeFilter

    val openDatePicker: LiveData<Boolean>
        get() = _openDatePicker


    init {
        _dateRange.value = Pair(
            System.currentTimeMillis(),
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(6)
        )
        _averageSizeFilter.value = Pair(AverageSize.SMALL, AverageSize.BIG)
        Timber.d("init FilterViewModel was called")
    }

    fun onOpenDatePicker(){
        _openDatePicker.value = true
    }
    fun openDatePickerDone(){
        _openDatePicker.value = false
    }

    fun setDateRange(range: Pair<Long, Long>){
        _dateRange.value = range
        Timber.d("setDateRange: $range")
    }

    fun hazardousSwitchIsChanged(isHazardous: Boolean) {
        _isHazardousFilter.value = isHazardous
    }

    fun averageSizeSliderIsChanged(sizeRange: Pair<AverageSize, AverageSize>){
        _averageSizeFilter.value = sizeRange
    }
}