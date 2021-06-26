package com.helios.maxwage.viewmodels

import androidx.lifecycle.*
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.Job
import com.helios.maxwage.models.JobSchedules
import com.helios.maxwage.models.ScheduleWrapper
import com.helios.maxwage.models.UserTimeTables
import com.helios.maxwage.repositories.JobRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.JWTUtils
import com.helios.maxwage.utils.JobScheduleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Created by Helios on 6/13/2021.
 */
class TimeTableFragmentViewModel : ViewModel() {

    private var _message: MutableLiveData<String> = MutableLiveData()
    private val jobRepository = JobRepository.getInstance()
    private val _listSchedule: MutableLiveData<List<ScheduleWrapper>> = MutableLiveData()
    val listSchedule: LiveData<List<ScheduleWrapper>> = _listSchedule
    val message: LiveData<String> = _message

    init {
        loadTimetable()
    }

    private fun loadTimetable() {
        val timeTables = SharedPrefs.savedTimeTables
        timeTables?.let {
            if (it.userId == JWTUtils.parseId()) {
                _listSchedule.value = it.listScheduleWrapper
            }
        }
    }

    fun buildListTimeTable(jobSchedules: JobSchedules) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))

        val accessToken = SharedPrefs.accessToken

        val scheduleWrappers = JobScheduleUtils.makeScheduleWrappersFromJobSchedules(jobSchedules)

        val fetchedJobs = mutableSetOf<Job>()

        try {
            coroutineScope {
                for (wrapper in scheduleWrappers) {
                    loop@ for (schedule in wrapper.listSchedule) {

                        val fetchJob = fetchedJobs.find { it._id == schedule.jobId }

                        if (fetchJob != null) {
                            schedule.jobTitle = fetchJob.name
                            schedule.jobSalary = fetchJob.salary
                        } else {
                            val jobFromDeferred =
                                async { jobRepository.getJobById(accessToken, schedule.jobId) }

                            val job = jobFromDeferred.await()
                            fetchedJobs.add(job.data!!)

                            schedule.jobTitle = job.data.name
                            schedule.jobSalary = job.data.salary
                        }
                    }
                }
                _listSchedule.postValue(scheduleWrappers)
            }
            this@liveData.emit(Resource.success(scheduleWrappers, "Build timetable success"))
        } catch (ex: Exception) {
            this@liveData.emit(Resource.error<Nothing>(ex.toString()))
        }
    }

    fun getScheduleId(position: Int): Long? {
        return try {
            _listSchedule.value?.get(position).hashCode().toLong()
        } catch (ex: IndexOutOfBoundsException) {
            null
        }
    }

    fun getScheduleSize(): Int {
        return _listSchedule.value?.size ?: 0
    }

    fun getScheduleAt(position: Int): ScheduleWrapper? {
        return try {
            listSchedule.value?.get(position)
        } catch (ex: IndexOutOfBoundsException) {
            null
        }
    }

    fun contains(itemId: Long): Boolean =
        listSchedule.value?.any { it.hashCode().toLong() == itemId } ?: false

    fun saveTimeTables() {
        val timetables = UserTimeTables(
            userId = JWTUtils.parseId()!!,
            listScheduleWrapper = listSchedule.value ?: listOf()
        )

        SharedPrefs.savedTimeTables = timetables
    }

    fun clearAllTimeTables() {
        _listSchedule.postValue(listOf())
    }

    fun removeOthers(currentPosition: Int) {
        val currentTimetable = _listSchedule.value!![currentPosition]

        _listSchedule.postValue(listOf(currentTimetable))
    }

    fun removeTimetableAt(currentPosition: Int) {
        val listTimetables: MutableList<ScheduleWrapper> = _listSchedule.value!! as MutableList
        listTimetables.removeAt(currentPosition)

        _listSchedule.postValue(listTimetables)
    }
}