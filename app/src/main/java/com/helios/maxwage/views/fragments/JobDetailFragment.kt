package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobDetailBinding
import com.helios.maxwage.viewmodels.JobDetailViewModel
import com.helios.maxwage.viewmodels.JobDetailViewModel.JobDetailViewModelFactory
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_JOB_ID = "jobId"

class JobDetailFragment : BaseFragment() {
    private var jobId: String? = null
    private lateinit var binding: FragmentJobDetailBinding
    private val viewModel: JobDetailViewModel by lazy {
        ViewModelProvider(
            this,
            JobDetailViewModelFactory(jobId!!)
        ).get(JobDetailViewModel::class.java)
    }

    override val TAG: String
        get() = "JobDetailFragment"

    override val shouldHideNavigationView: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobId = it.getString(ARG_JOB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)

        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        viewModel.job.observe(viewLifecycleOwner, {
            if (it.status == ApiStatus.SUCCESS) {
                binding.job = it.data
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(jobId: String) = JobDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_JOB_ID, jobId)
            }
        }
    }
}