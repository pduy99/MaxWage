package com.helios.maxwage.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.tlaabs.timetableview.Schedule
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentTimetableBinding
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.replace
import com.helios.maxwage.viewmodels.TimeTableFragmentViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.bottomsheet.SetTimeAvailableBottomSheet


class TimetableFragment : BaseFragment() {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimeTableFragmentViewModel by viewModels()

    override val TAG: String
        get() = "TimetableFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).onFabClicked = {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        initializeViewComponents()
        loadSaveSchedule()
        observeLiveData()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        SharedPrefs.savedJobSchedule = binding.timetable.createSaveData()
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
                        showJobSchedule(it.data)
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

    private fun loadSaveSchedule() {
        SharedPrefs.savedJobSchedule?.let {
            binding.timetable.load(it)
        }
    }

    private fun showJobSchedule(jobSchedules: ArrayList<Schedule>) {
        binding.timetable.add(jobSchedules)
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).showFab()

        with(binding) {
            timetable.setOnStickerSelectEventListener { idx, schedules ->
                val jobId = schedules[idx].jobId

                parentFragmentManager.replace(
                    JobDetailFragment.newInstance(jobId),
                    container = R.id.host_fragment,
                    allowAddToBackStack = true
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimetableFragment()
    }
}