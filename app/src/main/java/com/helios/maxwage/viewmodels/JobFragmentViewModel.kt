package com.helios.maxwage.viewmodels

import androidx.lifecycle.*
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.Job
import com.helios.maxwage.repositories.JobRepository
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.launch

/**
 * Created by Helios on 4/9/2021.
 */
class JobFragmentViewModel : ViewModel() {

    private var _jobs: MutableLiveData<Resource<List<Job>>> = MutableLiveData()
    var jobs: LiveData<Resource<List<Job>>> = _jobs

    private var _favoriteJobs: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    var favoriteJobs: LiveData<Resource<List<String>>> = _favoriteJobs

    private var jobRepository: JobRepository = JobRepository.getInstance()
    private var userRepository: UserRepository = UserRepository.getInstance()

    fun fetchJobs() = viewModelScope.launch {
        _jobs.postValue(Resource.loading(null))
        _jobs.postValue(jobRepository.getAllJobs(SharedPrefs.accessToken!!))
    }

    fun fetchFavoriteJobs() = viewModelScope.launch {
        _favoriteJobs.postValue(Resource.loading(null))
        _favoriteJobs.postValue(userRepository.getFavoriteJobs(SharedPrefs.accessToken!!))
    }

    fun addFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.addFavoriteJobs(SharedPrefs.accessToken!!, jobId))
    }

    fun removeFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.removeFavoriteJobs(SharedPrefs.accessToken!!, jobId))
    }
}