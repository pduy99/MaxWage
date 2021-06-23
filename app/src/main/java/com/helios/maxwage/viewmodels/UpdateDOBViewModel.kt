package com.helios.maxwage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.sharepreferences.SharedPrefs
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * Created by Helios on 6/18/2021.
 */
class UpdateDOBViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    fun updateMyBirthday(date: Date) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(userRepository.updateMyBirthday(SharedPrefs.accessToken, date))
    }
}