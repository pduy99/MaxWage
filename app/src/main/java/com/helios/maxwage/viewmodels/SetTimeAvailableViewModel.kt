package com.helios.maxwage.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.ServiceRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.JobScheduleUtils
import kotlinx.coroutines.Dispatchers

/**
 * Created by Helios on 3/17/2021.
 */
class SetTimeAvailableViewModel : ViewModel() {

    private val serviceRepository = ServiceRepository.getInstance()

    var message: MutableLiveData<String> = MutableLiveData()

    private val selectedFreeHour = Array(168) { false }

    fun buildFreeTimeArgument(selectedTime: Array<Boolean> = selectedFreeHour): Array<MutableList<String>> {
        return JobScheduleUtils.buildFreeTimeArgument(selectedTime)
    }

    fun scheduleJob(freeTime: Array<MutableList<String>>, onlyFavoriteJob: Boolean) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(
                serviceRepository.scheduleJob(
                    SharedPrefs.accessToken,
                    freeTime,
                    onlyFavoriteJob,
                    SharedPrefs.onlyJobsMatchSkills,
                    SharedPrefs.onlyJobsMatchAddress
                )
            )
        }

    companion object {
        const val MAX_SELECTED_TIME = 63
    }
}