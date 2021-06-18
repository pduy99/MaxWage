package com.helios.maxwage.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentUpdateDOBBinding
import com.helios.maxwage.utils.toString
import com.helios.maxwage.viewmodels.UpdateDOBViewModel
import com.helios.maxwage.views.base.BaseFragment
import java.util.*

private const val ARG_DOB = "param1"

class UpdateDOBFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentUpdateDOBBinding
    private lateinit var date: Date
    private val viewModel: UpdateDOBViewModel by viewModels()

    private var birthday: String? = null
    override val TAG: String
        get() = "UpdateDOBFragment"
    override val shouldHideNavigationView: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            birthday = it.getString(ARG_DOB)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateDOBBinding.inflate(inflater, container, false)

        initializeViewComponents()
        setupToolbar()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            edDOB.setOnClickListener {
                showDatePickerDialog()
            }

            btnUpdate.setOnClickListener {
                viewModel.updateMyBirthday(date).observe(viewLifecycleOwner, {
                    when (it.status) {
                        ApiStatus.LOADING -> {
                        }
                        ApiStatus.SUCCESS -> {
                            this@UpdateDOBFragment.showMessageToast(it.message!!)
                            parentFragmentManager.popBackStack()
                        }
                        ApiStatus.ERROR -> {
                            this@UpdateDOBFragment.showMessageToast("${it.message}")
                        }
                    }
                })
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), this@UpdateDOBFragment, mYear, mMonth, mDay).apply {
            show()
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
        fun newInstance(dob: String) =
            UpdateDOBFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOB, dob)
                }
            }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }.time

        binding.edDOB.setText(date.toString("dd/MM/yyyy"))
        binding.btnUpdate.isEnabled = true
    }
}