package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentAccountBinding
import com.helios.maxwage.utils.replace
import com.helios.maxwage.viewmodels.AccountViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton

class AccountFragment : BaseFragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()

    override val TAG: String
        get() = "AccountFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initializeViewComponents()
        fetchMyProfile()
        observeLiveData()
        return binding.root
    }

    private fun fetchMyProfile() {
        viewModel.fetchMyProfile()
    }

    private fun observeLiveData() {
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {

                }
                ApiStatus.SUCCESS -> {
                    binding.user = it.data
                    setupSkillsGroup(it.data!!.skills)
                }
                ApiStatus.ERROR -> {
                    Log.d(TAG, "${it.message}")
                }
            }
        })
    }

    private fun setupSkillsGroup(skills: List<String>) {
        with(binding) {
            skills.forEach { skill ->
                val btn = ThemedButton(requireContext())
                btn.text = skill
                val layout = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                layout.setMargins(0, 10, 10, 10)
                groupSkills.addView(btn, layout)
            }
        }
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).hideFab()
        with(binding) {
            edPhone.setOnClickListener {
                val currentPhoneNumber = edPhone.text.toString()
                parentFragmentManager.replace(
                    UpdatePhoneNumberFragment.newInstance(
                        currentPhoneNumber
                    ), container = R.id.host_fragment, allowAddToBackStack = true
                )
            }

            edAddress.setOnClickListener {
                val user = viewModel.user.value?.data

                user?.let {
                    parentFragmentManager.replace(
                        UpdateAddressFragment.newInstance(
                            user.addressCity ?: "",
                            user.addressDistrict ?: "",
                            user.addressWard ?: "",
                            user.addressHouseNumber ?: ""
                        ), container = R.id.host_fragment, allowAddToBackStack = true
                    )
                }
            }

            edDOB.setOnClickListener {
                val dob = edDOB.text.toString()
                parentFragmentManager.replace(
                    UpdateDOBFragment.newInstance(dob),
                    container = R.id.host_fragment,
                    allowAddToBackStack = true
                )
            }

            btnAddSkill.setOnClickListener {
                val user = viewModel.user.value?.data

                user?.let {
                    parentFragmentManager.replace(
                        UpdateSkillsFragment.newInstance(user.skills),
                        container = R.id.host_fragment,
                        allowAddToBackStack = true
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}