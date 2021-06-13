package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentTimetableBinding
import com.helios.maxwage.viewmodels.TimeTableFragmentViewModel
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.bottomsheet.SetTimeAvailableBottomSheet


class TimetableFragment : BaseFragment() {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimeTableFragmentViewModel by viewModels()

    override val TAG: String
        get() = "TimetableFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        initializeViewComponents()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        viewModel.schedule.observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {
                    this@TimetableFragment.showLoadingDialog("Building job schedule", "")
                }
                ApiStatus.SUCCESS -> {
                    val totalSalary = it.data!!.sumBy { job -> job.jobSalary }
                    this@TimetableFragment.hideLoadingDialog()
                    this@TimetableFragment.showMessageDialog(
                        "Create schedule completed",
                        "We have created a job schedule for you with maximum salary: $totalSalary"
                    ) {
                        binding.timetable.add(it.data)
                    }
                }
                ApiStatus.ERROR -> {
                    this@TimetableFragment.hideLoadingDialog()
                    this@TimetableFragment.showMessageDialog(
                        "You have encountered an error",
                        "There is an error while we were building your job schedule, please try again later!"
                    )
                }
            }
        })
    }

    private fun initializeViewComponents() {
        with(binding) {
            fabNewSchedule.setOnClickListener {
                SetTimeAvailableBottomSheet.newInstance().apply {
                    onNewSchedule = { jobSchedule ->
                        viewModel.buildJobSchedule(jobSchedule)
                    }
                }.show(
                    childFragmentManager,
                    SetTimeAvailableBottomSheet::class.java.name
                )
            }
        }

//        val schedules = ArrayList<Schedule>()
//        val schedule = Schedule()
//        schedule.classTitle = "Data Structure" // sets subject
//
//        schedule.day = 3
//
//        schedule.classPlace = "IT-601" // sets place
//
//        schedule.startTime = Time(12, 32) // sets the beginning of class time (hour,minute)
//
//        schedule.endTime = Time(16, 0) // sets the end of class time (hour,minute)
//
//        schedules.add(schedule)
//        binding.timetable.add(schedules)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimetableFragment()
    }
}