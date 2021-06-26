package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentRegisterBinding
import com.helios.maxwage.interfaces.IAuthenticationCallback
import com.helios.maxwage.utils.isEmailFormat
import com.helios.maxwage.utils.isValidPhoneNumber
import com.helios.maxwage.utils.validate
import com.helios.maxwage.viewmodels.RegisterViewModel
import com.helios.maxwage.views.base.BaseFragment

class RegisterFragment(var listener: IAuthenticationCallback) : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    override val TAG: String
        get() = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        initializeViewComponents()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        viewModel.message.observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {
                    this@RegisterFragment.showLoadingDialog("", "")
                }
                ApiStatus.SUCCESS -> {
                    this@RegisterFragment.hideLoadingDialog()
                    this@RegisterFragment.showMessageToast("${it.message}")
                    listener.showLoginScreen()
                }
                ApiStatus.ERROR -> {
                    this@RegisterFragment.hideLoadingDialog()
                    this@RegisterFragment.showMessageToast("${it.message}")
                }
            }
        })
    }

    private fun initializeViewComponents() {
        with(binding) {
            tvShowLogin.setOnClickListener {
                listener.showLoginScreen()
            }

            edFullName.validate(
                validator = { text -> text.isNotBlank() },
                "Full name cannot be blank",
                onInvalid = { btnRegister.isEnabled = false },
                onValid = { btnRegister.isEnabled = true })

            edPhone.validate(
                validator = { text -> text.isValidPhoneNumber() },
                "invalid phone number",
                onInvalid = { btnRegister.isEnabled = false },
                onValid = { btnRegister.isEnabled = true })

            edEmail.validate(
                validator = { text -> text.isEmailFormat() },
                "invalid email",
                onInvalid = { btnRegister.isEnabled = false },
                onValid = { btnRegister.isEnabled = true })

            edPassword.validate(
                validator = { text -> text.length > 8 },
                "Email length is minimum 8 characters",
                onInvalid = { btnRegister.isEnabled = false },
                onValid = { btnRegister.isEnabled = true })

            btnRegister.setOnClickListener {
                val fullName = edFullName.text.toString()
                val phone = edPhone.text.toString()
                val mail = edEmail.text.toString()
                val password = edPassword.text.toString()

                if (fullName.isNotBlank() && phone.isNotBlank() && mail.isNotBlank() && password.isNotBlank()) {
                    viewModel.register(fullName, phone, mail, password)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: IAuthenticationCallback) = RegisterFragment(listener)
    }
}