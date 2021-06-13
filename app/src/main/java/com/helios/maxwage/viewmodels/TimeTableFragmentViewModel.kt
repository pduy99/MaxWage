package com.helios.maxwage.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tlaabs.timetableview.Schedule
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.JobSchedule
import com.helios.maxwage.repositories.JobRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.JobScheduleUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Helios on 6/13/2021.
 */
class TimeTableFragmentViewModel : ViewModel() {

    private var _message: MutableLiveData<String> = MutableLiveData()
    private val jobRepository = JobRepository.getInstance()
    private val _schedule: MutableLiveData<Resource<ArrayList<Schedule>>> = MutableLiveData()
    val schedule = _schedule
    val message: LiveData<String> = _message

    fun buildJobSchedule(jobSchedule: JobSchedule) = viewModelScope.launch {
        _schedule.postValue(Resource.loading(null))

        val accessToken = SharedPrefs.accessToken!!
        val schedules = JobScheduleUtils.mapJobScheduleToSchedules(jobSchedule)
        try {
            coroutineScope {
                for (schedule in schedules) {
                    val jobFromDeferred =
                        async { jobRepository.getJobById(accessToken, schedule.jobId) }

                    val job = jobFromDeferred.await()

                    schedule.jobTitle = job.data!!.name
                    schedule.jobSalary = job.data.salary
                }
                _schedule.postValue(Resource.success(schedules, ""))
            }
        } catch (ex: Exception) {
            _schedule.postValue(Resource.error("Something went wrong"))
        }

    }
}