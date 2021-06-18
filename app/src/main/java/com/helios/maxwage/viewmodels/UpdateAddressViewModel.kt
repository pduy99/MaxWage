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
class UpdateAddressViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    fun updateMyAddress(city: String, district: String, ward: String, houseNumber: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(
                userRepository.updateMyAddress(
                    SharedPrefs.accessToken,
                    city,
                    district,
                    ward,
                    houseNumber
                )
            )
        }
}