package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.helios.maxwage.R
import com.helios.maxwage.adapters.ListJobAdapter
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobBinding
import com.helios.maxwage.utils.replace
import com.helios.maxwage.viewmodels.JobFragmentViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment

class JobFragment : BaseFragment() {

    private lateinit var binding: FragmentJobBinding
    private lateinit var viewModel: JobFragmentViewModel
    private lateinit var adapter: ListJobAdapter

    override val TAG: String
        get() = "JobFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(JobFragmentViewModel::class.java)

        initializeViewComponents()
        observeData()
        fetchData()

        return binding.root
    }

    private fun fetchData() {
        viewModel.fetchJobs()
        viewModel.fetchFavoriteJobs()
    }

    private fun observeData() {
        viewModel.jobs.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiStatus.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                ApiStatus.SUCCESS -> {
                    binding.tvTotalJob.text = it.data!!.size.toString()
                    adapter.jobs = it.data
                    adapter.notifyDataSetChanged()
                }
                ApiStatus.ERROR -> {
                    Log.d(TAG, "Error ${it.message}")
                }
            }
        })

        viewModel.favoriteJobs.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiStatus.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                ApiStatus.SUCCESS -> {
                    adapter.listFavorite = it.data!!
                    adapter.notifyDataSetChanged()
                }
                ApiStatus.ERROR -> {
                    Log.d(TAG, "Error ${it.message}")
                }
            }
        })
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).hideFab()
        with(binding) {
            adapter = ListJobAdapter(listOf(), listOf()).apply {
                onClick = {
                    parentFragmentManager.replace(
                        JobDetailFragment.newInstance(it),
                        container = R.id.host_fragment,
                        allowAddToBackStack = true
                    )
                }

                onAddFavoriteJob = { jobId ->
                    viewModel.addFavoriteJob(jobId).observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            ApiStatus.LOADING -> {

                            }
                            ApiStatus.SUCCESS -> {
                                viewModel.fetchFavoriteJobs()
                            }
                            ApiStatus.ERROR -> {
                                Log.d(TAG, "${it.message}")
                            }
                        }
                    })
                }

                onRemoveFavoriteJob = { jobId ->
                    viewModel.removeFavoriteJob(jobId).observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            ApiStatus.LOADING -> {

                            }
                            ApiStatus.SUCCESS -> {
                                viewModel.fetchFavoriteJobs()
                            }
                            ApiStatus.ERROR -> {
                                Log.d(TAG, "${it.message}")
                            }
                        }
                    })
                }
            }
            recyclerviewJobs.adapter = adapter
            recyclerviewJobs.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = JobFragment()
    }
}