package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.helios.maxwage.adapters.ListJobAdapter
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobBinding
import com.helios.maxwage.viewmodels.JobFragmentViewModel

class JobFragment : Fragment() {

    private lateinit var binding: FragmentJobBinding
    private lateinit var viewModel: JobFragmentViewModel
    private lateinit var adapter: ListJobAdapter

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
        with(binding) {
            adapter = ListJobAdapter(listOf(), listOf()).apply {
                onClick = {

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
        const val TAG = "JobFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = JobFragment()
    }
}