package com.helios.maxwage.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentLoginBinding
import com.helios.maxwage.interfaces.IAuthenticationCallback
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.viewmodels.LoginViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment

class LoginFragment(var listerner: IAuthenticationCallback) : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override val TAG: String
        get() = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        initializeViewComponents()
        setupLiveData()

        return binding.root
    }

    private fun setupLiveData() {
        viewModel.loginResultMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.isInputFieldsValid.observe(viewLifecycleOwner, Observer { valid ->
            binding.btnLogin.isEnabled = valid
        })
    }

    private fun initializeViewComponents() {
        with(binding) {
            tvShowRegister.setOnClickListener {
                listerner.showRegisterScreen()
            }

            edEmail.doAfterTextChanged {
                it?.let {
                    viewModel.validateEmailField(it.toString())
                }
            }

            edPassword.doAfterTextChanged {
                it?.let {
                    viewModel.validatePasswordField(it.toString())
                }
            }

            btnLogin.setOnClickListener {
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()

                viewModel.requestLogin(email, password).observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        ApiStatus.LOADING -> {
                            this@LoginFragment.showLoadingDialog("", "")
                        }
                        ApiStatus.SUCCESS -> {
                            this@LoginFragment.hideLoadingDialog()
                            SharedPrefs.accessToken = it.data!!.token
                            navigateToMainActivity()
                        }
                        ApiStatus.ERROR -> {
                            this@LoginFragment.hideLoadingDialog()
                            this@LoginFragment.showMessageToast("${it.message}")
                        }
                    }
                })
            }
        }
    }

    private fun navigateToMainActivity() {
        Intent(requireContext(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: IAuthenticationCallback) = LoginFragment(listener)
    }
}