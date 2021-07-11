package com.klekchyan.asteroidsnews.view.filter

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.concurrent.TimeUnit
import androidx.core.util.Pair

@SuppressLint("ParcelCreator")
class RangeDateValidator(private val limit: Int)
    : CalendarConstraints.DateValidator{

    private var picker: MaterialDatePicker<Pair<Long, Long>>? = null

    fun setDatePicker(newPicker: MaterialDatePicker<Pair<Long, Long>>){
        picker = newPicker
    }

    override fun isValid(date: Long): Boolean {
        val selection = picker?.selection
        if(selection != null){
            val startDate = selection.first
            val endDate = selection.second
            if(startDate != null && endDate != null){
                val numberOfDays = (limit - 1) * TimeUnit.DAYS.toMillis(1)
                val permissibleValue = startDate + numberOfDays
                if((date > permissibleValue) && (endDate > permissibleValue) ) return false
            }
        }
        return true
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
    }
}