package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.helios.maxwage.adapters.ListJobAdapter
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobBinding
import com.helios.maxwage.viewmodels.JobFragmentViewModel

class JobFragment : Fragment() {

    private lateinit var binding : FragmentJobBinding
    private lateinit var viewModel : JobFragmentViewModel
    private lateinit var adapter : ListJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container,false )
        viewModel = ViewModelProvider(this).get(JobFragmentViewModel::class.java)

        initializeViewComponents()
        observeData()
        fetchData()

        return binding.root
    }

    private fun fetchData() {
        viewModel.fetchJobs()
    }

    private fun observeData() {
        viewModel.jobs.observe(viewLifecycleOwner, {
            when(it.status) {
                ApiStatus.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                ApiStatus.SUCCESS -> {
                    adapter.jobs = it.data!!
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
            adapter = ListJobAdapter(listOf())
            recyclerviewJobs.adapter = adapter
            recyclerviewJobs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    companion object {
        const val TAG = "JobFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = JobFragment()
    }
}