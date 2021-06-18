package com.helios.maxwage.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.maxwage.api.Resource
import com.helios.maxwage.models.User
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.launch

/**
 * Created by Helios on 6/17/2021.
 */

class AccountViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    private var _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>> = _user

    fun fetchMyProfile() = viewModelScope.launch {
        _user.postValue(Resource.loading(null))
        _user.postValue(userRepository.getMyProfile(SharedPrefs.accessToken))
    }
}