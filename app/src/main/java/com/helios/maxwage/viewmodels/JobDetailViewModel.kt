package com.helios.maxwage.viewmodels

import androidx.lifecycle.*
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.Job
import com.helios.maxwage.repositories.JobRepository
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.launch

/**
 * Created by Helios on 6/14/2021.
 */
class JobDetailViewModel(jobId: String) : ViewModel() {
    private val jobRepository = JobRepository.getInstance()
    private var userRepository: UserRepository = UserRepository.getInstance()

    private var _jobId = jobId
    private val _job: MutableLiveData<Resource<Job>> = MutableLiveData()
    val job: LiveData<Resource<Job>> = _job

    init {
        getJobById(_jobId)
    }

    private fun getJobById(jobId: String) = viewModelScope.launch {
        val token = SharedPrefs.accessToken!!
        _job.postValue(jobRepository.getJobById(token, jobId))
    }

    fun addFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.addFavoriteJobs(SharedPrefs.accessToken, jobId))
    }

    fun removeFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.removeFavoriteJobs(SharedPrefs.accessToken, jobId))
    }

    class JobDetailViewModelFactory(private val jobId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobDetailViewModel::class.java)) {
                return JobDetailViewModel(jobId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}