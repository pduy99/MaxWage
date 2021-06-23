package com.helios.maxwage.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.helios.maxwage.models.WorkingShift

/**
 * Created by Helios on 6/8/2021.
 */
class SetFreeDaySessionViewModel : ViewModel() {

    var message: MutableLiveData<String> = MutableLiveData()

    val selectedWorkingShift = Array(168) { false }

    fun setSelectedWorkingShift(day: Int, shift: WorkingShift, isSelected: Boolean): Boolean {
        for (i in shift.timeRange) {
            selectedWorkingShift[(day * 24) + i] = isSelected
        }
        return true
    }
}