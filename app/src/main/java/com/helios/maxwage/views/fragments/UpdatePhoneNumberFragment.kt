package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentUpdatePhoneNumberBinding
import com.helios.maxwage.utils.isValidPhoneNumber
import com.helios.maxwage.utils.validate
import com.helios.maxwage.viewmodels.UpdatePhoneNumberViewModel
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_PHONE_NUMBER = "param1"

class UpdatePhoneNumberFragment : BaseFragment() {

    private lateinit var binding: FragmentUpdatePhoneNumberBinding
    private val viewModel: UpdatePhoneNumberViewModel by viewModels()

    private var phoneNumber: String? = null
    override val TAG: String
        get() = "UpdatePhoneFragment"
    override val shouldHideNavigationView: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            phoneNumber = it.getString(ARG_PHONE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdatePhoneNumberBinding.inflate(inflater, container, false)

        initializeViewComponents()
        setupToolbar()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            edPhone.setText(phoneNumber)
            edPhone.validate(
                { it.isValidPhoneNumber() },
                "Phone number is invalid",
                { btnUpdate.isEnabled = false },
                { btnUpdate.isEnabled = true })

            btnUpdate.setOnClickListener {
                viewModel.updateMyPhoneNumber(edPhone.text.toString()).observe(viewLifecycleOwner, {
                    when (it.status) {
                        ApiStatus.LOADING -> {
                        }
                        ApiStatus.SUCCESS -> {
                            this@UpdatePhoneNumberFragment.showMessageToast(it.message!!)
                            parentFragmentManager.popBackStack()
                        }
                        ApiStatus.ERROR -> {
                            this@UpdatePhoneNumberFragment.showMessageToast("${it.message}")
                        }
                    }
                })
            }
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(phoneNumber: String) =
            UpdatePhoneNumberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHONE_NUMBER, phoneNumber)
                }
            }
    }
}