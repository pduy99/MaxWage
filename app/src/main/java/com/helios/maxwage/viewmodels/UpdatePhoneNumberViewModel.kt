package com.helios.maxwage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.Dispatchers

/**
 * Created by Helios on 6/18/2021.
 */
class UpdatePhoneNumberViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    fun updateMyPhoneNumber(phoneNumber: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(userRepository.updateMyPhoneNumber(SharedPrefs.accessToken, phoneNumber))
    }
}