package com.klekchyan.asteroidsnews.view.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.klekchyan.asteroidsnews.databinding.FragmentFilterBinding


private val PICKER_TAG = "pickerTag"

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private val filterViewModel: FilterViewModel by viewModels({ requireActivity() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val validator = RangeDateValidator(7)
        val calendarConstraintsBuilder = CalendarConstraints.Builder()
        calendarConstraintsBuilder.setValidator(validator)

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Close approach date")
            .setSelection(
                Pair(
                    filterViewModel.dateRange.value?.first,
                    filterViewModel.dateRange.value?.second
                ))
            .setCalendarConstraints(calendarConstraintsBuilder.build())
            .build()

        validator.setDatePicker(picker)

        binding?.dateButton?.setOnClickListener {
            filterViewModel.onOpenDatePicker()
        }

        filterViewModel.openDatePicker.observe(viewLifecycleOwner, { isClicked ->
            if(isClicked){
                picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
                filterViewModel.openDatePickerDone()
            }
        })

        picker.addOnPositiveButtonClickListener { pair ->
            filterViewModel.setDateRange(pair.first to pair.second)
        }
        picker.addOnNegativeButtonClickListener {  }
        picker.addOnCancelListener {  }
        picker.addOnDismissListener {  }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}