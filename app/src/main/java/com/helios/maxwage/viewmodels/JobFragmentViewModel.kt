package com.helios.maxwage.viewmodels

import androidx.lifecycle.*
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.Job
import com.helios.maxwage.repositories.JobRepository
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    init {
        fetchAllJob()
    }

    fun fetchAllJob(
        selectedDistrict: List<String> = listOf(),
        minWage: Int = 0,
        selectedSkills: List<String> = listOf(),
        matchMySkillsOnly: Boolean = false,
        myFavoriteJobsOnly: Boolean = false
    ) = viewModelScope.launch {
        _jobs.postValue(Resource.loading(null))
        val token = SharedPrefs.accessToken

        try {
            coroutineScope {
                val jobsFromDeferred = async {
                    jobRepository.getAllJobs(
                        token,
                        selectedDistrict,
                        minWage,
                        selectedSkills,
                        matchMySkillsOnly,
                        myFavoriteJobsOnly
                    )
                }
                val favoriteJobFromDeferred = async { userRepository.getFavoriteJobs(token) }

                val favoriteJobs = favoriteJobFromDeferred.await()
                val jobs = jobsFromDeferred.await()

                favoriteJobs.data?.forEach { jobId ->
                    jobs.data?.find { it._id == jobId }?.isFavorite = true
                }

                _jobs.postValue(jobs)
                _favoriteJobs.postValue(favoriteJobs)
            }
        } catch (ex: Exception) {
            _jobs.postValue(ex.message?.let { Resource.error(it) })
        }
    }


    fun addFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.addFavoriteJobs(SharedPrefs.accessToken, jobId))
    }

    fun removeFavoriteJob(jobId: String) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.removeFavoriteJobs(SharedPrefs.accessToken, jobId))
    }
}