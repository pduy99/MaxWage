package com.helios.maxwage.viewmodels

import androidx.lifecycle.ViewModel
import com.helios.maxwage.utils.JobScheduleUtils

/**
 * Created by Helios on 6/10/2021.
 */
class SetFreeTimeViewModel : ViewModel() {

    val selectedFreeTime = Array(168) { false }

    fun addNewFreeTime(day: Int, startHour: Int, endHour: Int): List<String> {
        for (i in (startHour..endHour)) {
            selectedFreeTime[(day * 24) + i] = true
        }

        return JobScheduleUtils.buildFreeTimeArgument(selectedFreeTime)[day]
    }

    fun removeFreeTime(day: Int, startHour: Int, endHour: Int): List<String> {
        for (i in (startHour..endHour)) {
            selectedFreeTime[(day * 24) + i] = false
        }

        return JobScheduleUtils.buildFreeTimeArgument(selectedFreeTime)[day]
    }
}