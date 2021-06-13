package com.helios.maxwage.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.helios.maxwage.api.Resource
import com.helios.maxwage.repositories.UserRepository
import com.helios.maxwage.utils.string.StringUtils.isEmailFormat

/**
 * Created by Helios on 4/27/2021.
 */
class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository.getInstance()

    private var _isInputFieldsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val isInputFieldsValid: LiveData<Boolean> = _isInputFieldsValid

    private var _loginResultMessage: MutableLiveData<String> = MutableLiveData()
    val loginResultMessage: LiveData<String> = _loginResultMessage

    fun requestLogin(email: String, password: String) = liveData {
        emit(Resource.loading(null))
        userRepository.login(email, password).let {
            _loginResultMessage.postValue(it.message)
            emit(it)
        }
    }

    fun validateEmailField(email: String) {
        _isInputFieldsValid.value = email.isEmailFormat()
    }

    fun validatePasswordField(password: String) {
        _isInputFieldsValid.value = password.isNotEmpty()
    }
}