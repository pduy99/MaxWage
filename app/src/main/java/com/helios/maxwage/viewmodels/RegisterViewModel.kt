package com.helios.maxwage.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by Helios on 5/15/2021.
 */
class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()
    private val _message: MutableLiveData<Resource<String>> = MutableLiveData()
    val message: LiveData<Resource<String>> = _message

    fun register(fullName: String, phone: String, email: String, password: String) =
        viewModelScope.launch {
            _message.postValue(Resource.loading(null))
            _message.postValue(userRepository.register(fullName, phone, email, password))
        }


}