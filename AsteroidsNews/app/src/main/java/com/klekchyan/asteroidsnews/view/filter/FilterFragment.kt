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
import com.google.android.material.slider.RangeSlider
import com.klekchyan.asteroidsnews.databinding.FragmentFilterBinding
import com.klekchyan.asteroidsnews.network.AverageSize
import com.klekchyan.asteroidsnews.utils.DateType
import com.klekchyan.asteroidsnews.utils.dateTypeCast
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


private const val PICKER_TAG = "pickerTag"
private const val LIMIT_DATE_RANGE = 7

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private val filterViewModel: FilterViewModel by viewModels({ requireActivity() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated was called")

        binding?.viewModel = filterViewModel
        binding?.averageSizeSlider?.setLabelFormatter{ currentNumber ->
            getLabelFormatter(currentNumber)
        }
        binding?.changeableDateText?.setOnClickListener { view ->
            filterViewModel.onOpenDatePicker()
            view.isClickable = false
        }
        binding?.onlyHazardousSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            filterViewModel.hazardousSwitchIsChanged(isChecked)
        }
        binding?.averageSizeSlider?.addOnChangeListener { slider, value, fromUser ->
            val range = getAverageSizeRange(slider)
            filterViewModel.averageSizeSliderIsChanged(range)
        }

        filterViewModel.openDatePicker.observe(viewLifecycleOwner, { isClicked ->
            if(isClicked){
                val picker = getDateRangePicker()
                picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
                filterViewModel.openDatePickerDone()
            }
        })

        filterViewModel.dateRange.observe(viewLifecycleOwner, { range ->
            setChangeableDateRange(range)
        })

        filterViewModel.isHazardousFilter.observe(viewLifecycleOwner, { isHazardous ->
            binding?.onlyHazardousSwitch?.isChecked = isHazardous
        })

        filterViewModel.averageSizeFilter.observe(viewLifecycleOwner, { range ->
            binding?.averageSizeSlider?.values = listOf(
                                                    range.first.numericalValue,
                                                    range.second.numericalValue)
        })
    }

    private fun getDateRangePicker() : MaterialDatePicker<Pair<Long, Long>>{
        val validator = RangeDateValidator(LIMIT_DATE_RANGE)
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

        picker.addOnPositiveButtonClickListener { range ->
            filterViewModel.setDateRange(Pair(range.first, range.second))
            binding?.changeableDateText?.isClickable = true
        }
        picker.addOnNegativeButtonClickListener { binding?.changeableDateText?.isClickable = true }
        picker.addOnCancelListener { binding?.changeableDateText?.isClickable = true }
        picker.addOnDismissListener { binding?.changeableDateText?.isClickable = true }

        return picker
    }

    private fun setChangeableDateRange(range: Pair<Long, Long>){
        val startDate = dateTypeCast(Date(range.first), DateType.DATE)
        val endDate = dateTypeCast(Date(range.second), DateType.DATE)
        val string = "$startDate - $endDate"
        binding?.changeableDateText?.text = string
    }

    private fun getLabelFormatter(currentNumber: Float): String{
        return when(currentNumber){
            0f -> "Small"
            1f -> "Medium"
            else -> "Big"
        }
    }

    private fun getAverageSizeRange(range: RangeSlider): Pair<AverageSize, AverageSize>{
        val min = when(range.values.minOrNull()){
            0f -> AverageSize.SMALL
            1f -> AverageSize.MEDIUM
            else -> AverageSize.BIG
        }
        val max = when(range.values.maxOrNull()){
            0f -> AverageSize.SMALL
            1f -> AverageSize.MEDIUM
            else -> AverageSize.BIG
        }

        return Pair(min, max)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}