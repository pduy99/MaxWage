package com.helios.maxwage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs

/**
 * Created by Helios on 6/19/2021.
 */
class UpdateSkillsViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    fun updateMySkills(skills: List<String>) = liveData {
        emit(Resource.loading(null))
        emit(userRepository.updateMySkills(SharedPrefs.accessToken, skills))
    }
}