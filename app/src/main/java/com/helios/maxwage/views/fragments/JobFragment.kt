package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }

    private fun observeData() {
        viewModel.jobs.observe(viewLifecycleOwner, {
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

//        viewModel.favoriteJobs.observe(viewLifecycleOwner, Observer {
//            when (it.status) {
//                ApiStatus.LOADING -> {
//                    Log.d(TAG, "Loading")
//                }
//                ApiStatus.SUCCESS -> {
//                    adapter.listFavorite = it.data!!
//                    adapter.notifyDataSetChanged()
//                }
//                ApiStatus.ERROR -> {
//                    Log.d(TAG, "Error ${it.message}")
//                }
//            }
//        })
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).hideFab()
        with(binding) {
            adapter = ListJobAdapter(listOf()).apply {
                onClick = {
                    parentFragmentManager.replace(
                        JobDetailFragment.newInstance(it),
                        container = R.id.host_fragment,
                        allowAddToBackStack = true
                    )
                }

                onAddFavoriteJob = { jobId ->
                    viewModel.addFavoriteJob(jobId).observe(viewLifecycleOwner, {
                        when (it.status) {
                            ApiStatus.LOADING -> {

                            }
                            ApiStatus.SUCCESS -> {
                                val indexJob = adapter.jobs.indexOfFirst { job -> job._id == jobId }
                                if(indexJob != -1) {
                                    adapter.jobs[indexJob].isFavorite = true
                                    adapter.notifyItemChanged(indexJob)
                                }
                            }
                            ApiStatus.ERROR -> {
                                Log.d(TAG, "${it.message}")
                            }
                        }
                    })
                }

                onRemoveFavoriteJob = { jobId ->
                    viewModel.removeFavoriteJob(jobId).observe(viewLifecycleOwner, {
                        when (it.status) {
                            ApiStatus.LOADING -> {

                            }
                            ApiStatus.SUCCESS -> {
                                val indexJob = adapter.jobs.indexOfFirst { job -> job._id == jobId }
                                if(indexJob != -1) {
                                    adapter.jobs[indexJob].isFavorite = false
                                    adapter.notifyItemChanged(indexJob)
                                }
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
        fun newInstance() = JobFragment()
    }
}