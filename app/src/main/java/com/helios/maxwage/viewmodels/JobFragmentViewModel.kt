package com.helios.maxwage.viewmodels

import androidx.lifecycle.*
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.Job
import com.helios.maxwage.repositories.JobRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Helios on 4/9/2021.
 */
class JobFragmentViewModel : ViewModel() {

    private var _jobs : MutableLiveData<Resource<List<Job>>> = MutableLiveData()
    var jobs : LiveData<Resource<List<Job>>> = _jobs

    private var jobRepository: JobRepository = JobRepository.getInstance()

    fun fetchJobs() = viewModelScope.launch {
        _jobs.postValue(Resource.loading(null))
        _jobs.postValue(jobRepository.getAllJobs())
    }
}