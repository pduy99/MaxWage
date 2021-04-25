package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.helios.maxwage.databinding.FragmentTimetableBinding


class TimetableFragment : Fragment() {

    private lateinit var binding: FragmentTimetableBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        initializeViewComponents()

        return binding.root
    }

    private fun initializeViewComponents() {
        val schedules = ArrayList<Schedule>()
        val schedule = Schedule()
        schedule.classTitle = "Data Structure" // sets subject

        schedule.day = 3

        schedule.classPlace = "IT-601" // sets place

        schedule.startTime = Time(12, 32) // sets the beginning of class time (hour,minute)

        schedule.endTime = Time(16, 0) // sets the end of class time (hour,minute)

        schedules.add(schedule)
        binding.timetable.add(schedules)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimetableFragment()
    }
}