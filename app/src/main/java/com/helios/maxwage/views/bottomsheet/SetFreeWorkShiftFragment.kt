package com.helios.maxwage.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.helios.maxwage.R
import com.helios.maxwage.databinding.FragmentSetFreeWorkShiftBinding
import com.helios.maxwage.interfaces.IButtonScheduleClick
import com.helios.maxwage.models.FirstWorkingSift
import com.helios.maxwage.models.SecondWorkingSift
import com.helios.maxwage.models.ThirdWorkingSift
import com.helios.maxwage.utils.Constants.MAX_WORKING_TIME
import com.helios.maxwage.viewmodels.SetFreeWorkShiftViewModel
import com.helios.maxwage.views.base.BaseFragment

class SetFreeWorkShiftFragment(var listener: IButtonScheduleClick) : BaseFragment() {

    private lateinit var binding: FragmentSetFreeWorkShiftBinding
    private val viewModel: SetFreeWorkShiftViewModel by viewModels()

    override val TAG: String
        get() = "WorkShiftFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetFreeWorkShiftBinding.inflate(inflater, container, false)

        initializeViewComponents()
        onAvailableTimeSelected()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            btnScheduleJob.setOnClickListener {
                listener.onButtonScheduleClick(
                    viewModel.selectedWorkingShift,
                    binding.switchOnlyFavoriteJob.isSelected
                )
            }
        }
    }

    private fun onAvailableTimeSelected() {
        binding.firstShift.apply {
            this.setOnSelectListener { button ->
                val index = this.indexOfChild(button)
                viewModel.setSelectedWorkingShift(
                    day = index,
                    shift = FirstWorkingSift(),
                    isSelected = button.isSelected
                )
                updateTotalHourText()
            }
        }

        binding.secondShift.apply {
            this.setOnSelectListener { button ->
                val index = this.indexOfChild(button)
                viewModel.setSelectedWorkingShift(
                    day = index,
                    shift = SecondWorkingSift(),
                    isSelected = button.isSelected
                )
                updateTotalHourText()
            }
        }

        binding.thirdShift.apply {
            this.setOnSelectListener { button ->
                val index = this.indexOfChild(button)
                viewModel.setSelectedWorkingShift(
                    day = index,
                    shift = ThirdWorkingSift(),
                    isSelected = button.isSelected
                )
                updateTotalHourText()
            }
        }
    }

    private fun updateTotalHourText() {
        with(binding) {
            val totalHour = viewModel.selectedWorkingShift.count { it }
            if (totalHour > MAX_WORKING_TIME) {
                tvTotalHour.text =
                    getString(R.string.maximum_working_hour_alert, MAX_WORKING_TIME, totalHour)
                tvTotalHour.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                btnScheduleJob.isEnabled = false
            } else {
                tvTotalHour.text = getString(R.string.total_working_hour_message, totalHour)
                tvTotalHour.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
                btnScheduleJob.isEnabled = true
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: IButtonScheduleClick) =
            SetFreeWorkShiftFragment(listener)
    }
}