package com.helios.maxwage.views.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.helios.maxwage.databinding.DialogChooseStartEndHourBinding

/**
 * Created by Helios on 6/11/2021.
 */
class FreeTimePickerDialog private constructor() : DialogFragment() {

    private lateinit var binding: DialogChooseStartEndHourBinding
    private val hoursArray: List<String> by lazy {
        (0..24).map {
            if (it == 24) {
                "00:00"
            } else {
                it.toString().padStart(2) + ":00"
            }
        }
    }
    private var startTimeIndex = 0
    private var endTimeIndex = 1

    var onFreeTimeSet: ((startHour: Int, endHour: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogChooseStartEndHourBinding.inflate(inflater, container, false)

        initData()
        initializeViewComponents()

        return binding.root

    }

    private fun initData() {

        // Start Time init with hour array without last value
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            hoursArray.dropLast(1)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerStartTime.adapter = adapter
        }

        // End Time init with hour array without first value
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            hoursArray.drop(1)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEndTime.adapter = adapter
        }
    }

    private fun initializeViewComponents() {
        with(binding) {
            spinnerStartTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    selectedView: View?,
                    position: Int,
                    id: Long
                ) {
                    startTimeIndex = position
                    resetEndTimeSpinnerIfNeed(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }

            }

            spinnerEndTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    selectedView: View?,
                    position: Int,
                    id: Long
                ) {
                    endTimeIndex = hoursArray.indexOf(spinnerEndTime.selectedItem.toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }

            }

            btnSet.setOnClickListener {
                onFreeTimeSet?.invoke(startTimeIndex, endTimeIndex)
                this@FreeTimePickerDialog.dismiss()
            }

            btnCancel.setOnClickListener {
                this@FreeTimePickerDialog.dismiss()
            }
        }
    }

    private fun resetEndTimeSpinnerIfNeed(position: Int) {
        val endHoursArray = hoursArray.drop(position + 1)
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            endHoursArray
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEndTime.adapter = adapter
        }

        if (position > endTimeIndex) {
            endTimeIndex = position + 1
        } else {
            val currentValue = hoursArray[endTimeIndex]
            binding.spinnerEndTime.setSelection(endHoursArray.indexOf(currentValue))
        }
    }

    companion object {
        fun newInstance() = FreeTimePickerDialog()

        const val TAG = "FreeTimePickerDialog"
    }

}