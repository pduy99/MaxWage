package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobDetailBinding
import com.helios.maxwage.viewmodels.JobDetailViewModel
import com.helios.maxwage.viewmodels.JobDetailViewModel.JobDetailViewModelFactory
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_JOB_ID = "jobId"
private const val ARG_IS_FAVORITE = "isFavorite"

class JobDetailFragment : BaseFragment() {

    private var jobId: String? = null
    private var isFavorite: Boolean? = null

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
        setHasOptionsMenu(true)
        arguments?.let {
            jobId = it.getString(ARG_JOB_ID)
            isFavorite = it.getBoolean(ARG_IS_FAVORITE)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideFab()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)

        initializeViewComponents()
        observeLiveData()
        setupToolbar()

        return binding.root
    }

    private fun initializeViewComponents() {
        with(binding) {
            if (isFavorite == true) {
                btnToggleFavorite.setIconResource(R.drawable.ic_favorite_24)
            } else {
                btnToggleFavorite.setIconResource(R.drawable.ic_favorite_border)
            }

            btnToggleFavorite.setOnClickListener {
                if (isFavorite == true) {
                    viewModel.removeFavoriteJob(jobId!!).observe(viewLifecycleOwner, {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                            }
                            ApiStatus.SUCCESS -> {
                                isFavorite = false
                                btnToggleFavorite.setIconResource(R.drawable.ic_favorite_border)
                            }
                            ApiStatus.ERROR -> {
                            }
                        }
                    })
                } else {
                    viewModel.addFavoriteJob(jobId!!).observe(viewLifecycleOwner, {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                            }
                            ApiStatus.SUCCESS -> {
                                isFavorite = false
                                btnToggleFavorite.setIconResource(R.drawable.ic_favorite_24)
                            }
                            ApiStatus.ERROR -> {
                            }
                        }
                    })
                }
            }
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun observeLiveData() {
        viewModel.job.observe(viewLifecycleOwner, {
            if (it.status == ApiStatus.SUCCESS) {
                binding.job = it.data

                Log.d(TAG, Gson().toJson(it))
            }
        })
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
        fun newInstance(jobId: String, isFavorite: Boolean) = JobDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_JOB_ID, jobId)
                putBoolean(ARG_IS_FAVORITE, isFavorite)
            }
        }
    }
}