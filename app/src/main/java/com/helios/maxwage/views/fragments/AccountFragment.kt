package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentAccountBinding
import com.helios.maxwage.viewmodels.AccountViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment

class AccountFragment : BaseFragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel : AccountViewModel by viewModels()

    override val TAG: String
        get() = "AccountFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initializeViewComponents()
        observeLiveData()
        return binding.root
    }

    private fun observeLiveData() {
        viewModel.user.observe(viewLifecycleOwner, {
            when(it.status) {
                ApiStatus.LOADING -> {

                }
                ApiStatus.SUCCESS -> {
                    binding.user = it.data
                }
                ApiStatus.ERROR -> {
                    Log.d(TAG, "${it.message}")
                }
            }
        })
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).hideFab()
        with(binding){
            edPhone.setOnClickListener {
                //
            }

            edAddress.setOnClickListener {

            }

            edDOB.setOnClickListener {

            }

            btnAddSkill.setOnClickListener {

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}