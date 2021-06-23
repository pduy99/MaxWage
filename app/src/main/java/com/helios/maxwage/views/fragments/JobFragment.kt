package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helios.maxwage.R
import com.helios.maxwage.adapters.ListJobAdapter
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentJobBinding
import com.helios.maxwage.models.Job
import com.helios.maxwage.utils.replace
import com.helios.maxwage.viewmodels.JobFragmentViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.bottomsheet.JobFilterBottomSheet

class JobFragment : BaseFragment() {

    private lateinit var binding: FragmentJobBinding
    private lateinit var viewModel: JobFragmentViewModel
    private lateinit var adapter: ListJobAdapter

    private var _selectedDistrict: List<String> = listOf()
    private var _minWage: Int = 0
    private var _selectedSkills: List<String> = listOf()
    private var _matchMySkillsOnly: Boolean = false
    private var _myFavoriteJobsOnly: Boolean = false

    override val TAG: String
        get() = "JobFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(JobFragmentViewModel::class.java)

        initializeViewComponents()
        fetchJobs()
        setupToolbar()
        observeData()

        return binding.root
    }

    private fun fetchJobs() {
        viewModel.fetchAllJob(
            _selectedDistrict,
            _minWage,
            _selectedSkills,
            _matchMySkillsOnly,
            _myFavoriteJobsOnly
        )
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun observeData() {
        viewModel.jobs.observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                ApiStatus.SUCCESS -> {
                    binding.tvTotalJob.text = it.data!!.size.toString()
                    setupJobRecyclerView(it.data)
                    binding.swiperefresh.isRefreshing = false
                }
                ApiStatus.ERROR -> {
                    Log.d(TAG, "Error ${it.message}")
                    binding.swiperefresh.isRefreshing = false
                }
            }
        })
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).hideFab()
        with(binding) {
            swiperefresh.setOnRefreshListener {
                fetchJobs()
            }
        }
    }

    private fun setupJobRecyclerView(jobs: List<Job>) {
        adapter = ListJobAdapter(jobs).apply {

            val jobAdapterObserver = JobAdapterObserver()
            registerAdapterDataObserver(jobAdapterObserver)

            onClick = { jobId, isFavorite ->
                parentFragmentManager.replace(
                    JobDetailFragment.newInstance(jobId, isFavorite),
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
                            val indexJob =
                                adapter.getData().indexOfFirst { job -> job._id == jobId }
                            if (indexJob != -1) {
                                adapter.getData()[indexJob].isFavorite = true
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
                            val indexJob =
                                adapter.getData().indexOfFirst { job -> job._id == jobId }
                            if (indexJob != -1) {
                                adapter.getData()[indexJob].isFavorite = false
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
        binding.recyclerviewJobs.adapter = adapter
        binding.recyclerviewJobs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.jobs_page_menu, menu)

        val searchView = menu.findItem(R.id.menu_item_search_job).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                adapter.filter.filter(searchText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_filter -> {
                JobFilterBottomSheet.getInstance().apply {
                    onApplyFilter =
                        { selectedDistrict, minWage, selectedSkills, matchMySkillsOnly, myFavoriteJobsOnly ->
                            viewModel.fetchAllJob(
                                selectedDistrict,
                                minWage,
                                selectedSkills,
                                matchMySkillsOnly,
                                myFavoriteJobsOnly
                            )

                            _selectedDistrict = selectedSkills
                            _minWage = minWage
                            _selectedSkills = selectedSkills
                            _matchMySkillsOnly = matchMySkillsOnly
                            _myFavoriteJobsOnly = myFavoriteJobsOnly
                        }
                }.show(childFragmentManager, JobFilterBottomSheet::class.java.name)

                true
            }

            else -> {
                false
            }
        }
    }

    inner class JobAdapterObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            binding.tvTotalJob.text = adapter.getData().size.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = JobFragment()
    }
}