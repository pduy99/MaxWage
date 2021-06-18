package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentUpdateAddressBinding
import com.helios.maxwage.viewmodels.UpdateAddressViewModel
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_CITY = "city"
private const val ARG_DISTRICT = "district"
private const val ARG_WARD = "ward"
private const val ARG_HOUSE_NUMBER = "houseNumber"

class UpdateAddressFragment : BaseFragment() {

    private lateinit var binding: FragmentUpdateAddressBinding
    private val viewModel: UpdateAddressViewModel by viewModels()

    private var city: String? = null
    private var district: String? = null
    private var ward: String? = null
    private var houseNumber: String? = null

    override val TAG: String
        get() = "UpdateDOBFragment"
    override val shouldHideNavigationView: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            city = it.getString(ARG_CITY)
            district = it.getString(ARG_DISTRICT)
            ward = it.getString(ARG_WARD)
            houseNumber = it.getString(ARG_HOUSE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateAddressBinding.inflate(inflater, container, false)

        initializeViewComponents()
        setupToolbar()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            edCity.setText(city)
            edDistrict.setText(district)
            edWard.setText(ward)
            edHouseNumber.setText(houseNumber)

            val cityAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.support_City)
            )
            edCity.setAdapter(cityAdapter)

            val districtAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.HCMC_District)
            )
            edDistrict.setAdapter(districtAdapter)

            btnUpdate.setOnClickListener {
                val city = edCity.text.toString()
                val district = edDistrict.text.toString()
                val ward = edWard.text.toString()
                val houseNumber = edHouseNumber.text.toString()
                viewModel.updateMyAddress(city, district, ward, houseNumber)
                    .observe(viewLifecycleOwner, {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                            }
                            ApiStatus.SUCCESS -> {
                                this@UpdateAddressFragment.showMessageToast(it.message!!)
                                parentFragmentManager.popBackStack()
                            }
                            ApiStatus.ERROR -> {
                                this@UpdateAddressFragment.showMessageToast("${it.message}")
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
        fun newInstance(city: String, district: String, ward: String, houseNumber: String) =
            UpdateAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY, city)
                    putString(ARG_DISTRICT, district)
                    putString(ARG_WARD, ward)
                    putString(ARG_HOUSE_NUMBER, houseNumber)
                }
            }
    }

}