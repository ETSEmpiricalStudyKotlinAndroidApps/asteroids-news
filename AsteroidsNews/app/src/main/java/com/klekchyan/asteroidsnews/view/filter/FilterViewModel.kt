package com.klekchyan.asteroidsnews.view.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klekchyan.asteroidsnews.network.AverageSize

class FilterViewModel: ViewModel() {

    private val _dateRange = MutableLiveData<Pair<Long, Long>>()
    private val _isHazardous = MutableLiveData<Boolean>()
    private val _size = MutableLiveData<AverageSize>()

    private val _openDatePicker = MutableLiveData(false)

    val dateRange: LiveData<Pair<Long, Long>>
        get() = _dateRange
    val isHazardous: LiveData<Boolean>
        get() = _isHazardous
    val size: LiveData<AverageSize>
        get() = _size
    val openDatePicker: LiveData<Boolean>
        get() = _openDatePicker

    fun onOpenDatePicker(){
        _openDatePicker.value = true
    }
    fun openDatePickerDone(){
        _openDatePicker.value = false
    }

    fun setDateRange(range: Pair<Long, Long>){
        _dateRange.value = range
    }
}