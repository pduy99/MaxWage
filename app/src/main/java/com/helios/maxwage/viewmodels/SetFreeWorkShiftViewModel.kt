package com.helios.maxwage.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.helios.maxwage.models.WorkingShift

/**
 * Created by Helios on 6/8/2021.
 */
class SetFreeWorkShiftViewModel : ViewModel() {

    var message: MutableLiveData<String> = MutableLiveData()

    val selectedWorkingShift = Array(168) { false }

    fun setSelectedWorkingShift(day: Int, shift: WorkingShift, isSelected: Boolean): Boolean {
        if (selectedWorkingShift.count { it } >= SetTimeAvailableViewModel.MAX_SELECTED_TIME) {
            message.postValue("Max time is 63 hours")
            return false
        }
        for (i in shift.timeRange) {
            selectedWorkingShift[(day * 24) + i] = isSelected
        }
        return true
    }
}