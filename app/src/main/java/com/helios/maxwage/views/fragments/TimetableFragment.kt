package com.helios.maxwage.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.helios.maxwage.utils.toCurrencyFormat
import com.helios.maxwage.viewmodels.TimeTableFragmentViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.bottomsheet.SetTimeAvailableBottomSheet


class TimetableFragment : BaseFragment() {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimeTableFragmentViewModel by viewModels()

    // A workaround to fix fragment create new instance of Observer to observer schedule
    // after pop back stack from job detail fragment
    private var shouldObserveData: Boolean = true

    override val TAG: String
        get() = "TimetableFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).onFabClicked = {
                if (binding.timetable.allSchedulesInStickers.size != 0) {
                    this@TimetableFragment.showConfirmDialog(
                        requireContext(),
                        "Create new schedule",
                        "Your are going to create a new job schedule, which will delete current schedule. Are you sure you want to continue?",
                        positiveText = "CONTINUE",
                        negativeText = "CANCEL",
                        actionOnAgree = {
                            SetTimeAvailableBottomSheet.newInstance().apply {
                                onNewSchedule = { jobSchedule ->
                                    viewModel.buildJobSchedule(jobSchedule)
                                }
                            }.show(
                                childFragmentManager,
                                SetTimeAvailableBottomSheet::class.java.name
                            )
                        }
                    )
                } else {
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

    private fun observeLiveData() {

        viewModel.schedule.observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {
                    this@TimetableFragment.showLoadingDialog("Building job schedule", "")
                }
                ApiStatus.SUCCESS -> {
                    if (shouldObserveData) {
                        val totalSalary = it.data!!.sumBy { job -> job.jobSalary }
                        this@TimetableFragment.hideLoadingDialog()
                        this@TimetableFragment.showMessageDialog(
                            "Create schedule completed",
                            "We have created a job schedule for you with maximum salary: ${totalSalary.toCurrencyFormat()} Weekly"
                        ) {
                            SharedPrefs.savedJobSchedule = it.data
                            showJobSchedule(it.data)
                        }
                    } else {
                        shouldObserveData = true
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
            binding.timetable.add(it)
        }
    }

    private fun showJobSchedule(jobSchedules: ArrayList<Schedule>) {
        binding.timetable.add(jobSchedules)
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).showFab()
        (activity as MainActivity).showNavigationView()
        with(binding) {
            timetable.setOnStickerSelectEventListener { index, schedules ->
                try {
                    val jobId = schedules[index].jobId

                    parentFragmentManager.replace(
                        JobDetailFragment.newInstance(jobId, false),
                        container = R.id.host_fragment,
                        allowAddToBackStack = true
                    )
                    shouldObserveData = false
                } catch (ex: Exception) {
                    Log.d(TAG, ex.printStackTrace().toString())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimetableFragment()
    }
}