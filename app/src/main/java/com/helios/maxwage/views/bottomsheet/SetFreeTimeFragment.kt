package com.helios.maxwage.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.helios.maxwage.R
import com.helios.maxwage.databinding.FragmentSetFreeTimeBinding
import com.helios.maxwage.epoxycontroller.SetFreeTimeController
import com.helios.maxwage.interfaces.IButtonScheduleClick
import com.helios.maxwage.models.epoxy.WeekDayHeader
import com.helios.maxwage.utils.Constants
import com.helios.maxwage.viewmodels.SetFreeTimeViewModel
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.dialog.FreeTimePickerDialog

class SetFreeTimeFragment(val listener: IButtonScheduleClick) : BaseFragment() {

    private lateinit var binding: FragmentSetFreeTimeBinding
    private val viewModel: SetFreeTimeViewModel by viewModels()
    private val setFreeTimeController: SetFreeTimeController by lazy {
        SetFreeTimeController()
    }
    private val listDay: List<WeekDayHeader> by lazy {
        listOf(
            WeekDayHeader(0, "Monday", false, mutableListOf(), true),
            WeekDayHeader(1, "Tuesday", false, mutableListOf(), true),
            WeekDayHeader(2, "Wednesday", false, mutableListOf(), true),
            WeekDayHeader(3, "Thursday", false, mutableListOf(), true),
            WeekDayHeader(4, "Friday", false, mutableListOf(), true),
            WeekDayHeader(5, "Saturday", false, mutableListOf(), true),
            WeekDayHeader(6, "Sunday", false, mutableListOf(), true),
        )
    }

    override val TAG: String
        get() = "FreeTimeFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSetFreeTimeBinding.inflate(inflater, container, false)

        initializeViewComponents()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            recyclerviewWeekday.adapter = setFreeTimeController.adapter
            setFreeTimeController.apply {
                setData(listDay)
                onDeleteHour = { dayIndex, startHour, endHour ->
                    val freeTimeInDay = viewModel.removeFreeTime(dayIndex, startHour, endHour)
                    listDay[dayIndex].hours.apply {
                        clear()
                        addAll(freeTimeInDay)
                    }
                    updateTotalHourText()
                    setFreeTimeController.setData(listDay)
                }
                addNewHour = { dayIndex ->
                    FreeTimePickerDialog.newInstance().apply {
                        isCancelable = false
                        onFreeTimeSet = { startHour, endHour ->
                            val freeTimeInDay =
                                viewModel.addNewFreeTime(dayIndex, startHour, endHour)
                            listDay[dayIndex].hours.apply {
                                clear()
                                addAll(freeTimeInDay)
                            }
                            updateTotalHourText()
                            setFreeTimeController.setData(listDay)
                        }
                    }.show(childFragmentManager, FreeTimePickerDialog::class.java.name)
                }
            }

            btnScheduleJob.setOnClickListener {
                listener.onButtonScheduleClick(
                    viewModel.selectedFreeTime,
                    switchOnlyFavoriteJob.isChecked || binding.switchOnlyFavoriteJob.isSelected
                )
            }
        }
    }

    private fun updateTotalHourText() {
        with(binding) {
            val totalHour = viewModel.selectedFreeTime.count { it }
            if (totalHour > Constants.MAX_WORKING_TIME) {
                tvTotalHour.text = getString(
                    R.string.recommended_working_hour_alert,
                    Constants.MAX_WORKING_TIME, totalHour
                )
                tvTotalHour.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                tvTotalHour.text = getString(R.string.total_working_hour_message, totalHour)
                tvTotalHour.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: IButtonScheduleClick) = SetFreeTimeFragment(listener)
    }
}