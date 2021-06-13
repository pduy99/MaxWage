package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.helios.maxwage.databinding.FragmentRegisterBinding
import com.helios.maxwage.interfaces.ILoginCallback
import com.helios.maxwage.viewmodels.RegisterViewModel
import com.helios.maxwage.views.base.BaseFragment

class RegisterFragment(var listener: ILoginCallback) : BaseFragment() {

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

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            tvShowLogin.setOnClickListener {
                listener.showLoginScreen()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: ILoginCallback) = RegisterFragment(listener)
    }
}