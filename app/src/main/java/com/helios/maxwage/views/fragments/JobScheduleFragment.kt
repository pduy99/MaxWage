package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.helios.maxwage.databinding.FragmentJobScheduleBinding
import com.helios.maxwage.interfaces.OnJobClick
import com.helios.maxwage.models.ScheduleWrapper
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_JOB_SCHEDULE = "jobSchedule"

class JobScheduleFragment(val listener: OnJobClick) : BaseFragment() {

    private var jobSchedule: ScheduleWrapper? = null

    private lateinit var binding: FragmentJobScheduleBinding
    override val TAG: String
        get() = "JobScheduleFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val jsonString = it.getString(ARG_JOB_SCHEDULE)
            val turnsType = object : TypeToken<ScheduleWrapper>() {}.type
            jobSchedule = GsonBuilder().create().fromJson<ScheduleWrapper>(jsonString, turnsType)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentJobScheduleBinding.inflate(inflater, container, false)

        binding.timetable.add(jobSchedule!!.listSchedule)

        initializeViewComponents()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            timetable.setOnStickerSelectEventListener { index, schedules ->
                try {
                    val jobId = schedules[index].jobId

                    listener.onJobClick(jobId, false)

                } catch (ex: Exception) {
                    Log.d(TAG, ex.printStackTrace().toString())
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(jobSchedule: ScheduleWrapper, listener: OnJobClick) =
            JobScheduleFragment(listener).apply {
                arguments = Bundle().apply {
                    val jobScheduleStr = GsonBuilder().create().toJson(jobSchedule)
                    putString(ARG_JOB_SCHEDULE, jobScheduleStr)
                }
            }
    }
}