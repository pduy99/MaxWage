package com.helios.maxwage.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentTimetableBinding
import com.helios.maxwage.interfaces.OnJobClick
import com.helios.maxwage.models.JobSchedules
import com.helios.maxwage.models.ScheduleWrapper
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.replace
import com.helios.maxwage.utils.toCurrencyFormat
import com.helios.maxwage.utils.toString
import com.helios.maxwage.viewmodels.TimeTableFragmentViewModel
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment
import com.helios.maxwage.views.bottomsheet.SetTimeAvailableBottomSheet

class TimetableFragment : BaseFragment(), OnJobClick {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimeTableFragmentViewModel by viewModels()

    // A workaround to fix fragment create new instance of Observer to observer schedule
    // after pop back stack from job detail fragment
    private var shouldObserveData: Boolean = true

    override val TAG: String
        get() = "TimetableFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).onFabClicked = {
                if (viewModel.getScheduleSize() != 0) {
                    this@TimetableFragment.showConfirmDialog(
                        requireContext(),
                        getString(R.string.create_new_schedule),
                        getString(R.string.create_new_schedule_warning),
                        positiveText = getString(R.string.continue_btn_text),
                        negativeText = getString(R.string.cancel),
                        actionOnAgree = {
                            showSetTimeAvailableDialog()
                        }
                    )
                } else {
                    showSetTimeAvailableDialog()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        setupToolbar()
        initializeViewComponents()
        observeLiveData()

        return binding.root
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    override fun onPause() {
        super.onPause()
        saveTimetables()
    }

    private fun observeLiveData() {
        viewModel.listSchedule.observe(viewLifecycleOwner, {
            binding.viewPager.adapter!!.notifyDataSetChanged()
            if (viewModel.getScheduleSize() == 0) {
                binding.tvTimeTableOrder.text = getString(R.string.empty_timetable)
            } else {
                binding.tvTimeTableOrder.text = getString(
                    R.string.timetable_order,
                    binding.viewPager.currentItem + 1,
                    viewModel.getScheduleSize()
                )
            }
        })
    }

    private fun initializeViewComponents() {
        (activity as MainActivity).showFab()
        (activity as MainActivity).showNavigationView()
        with(binding) {
            viewPager.adapter = createViewPagerAdapter()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (viewModel.getScheduleSize() == 0) {
                        binding.tvTimeTableOrder.text = getString(R.string.empty_timetable)
                    } else {
                        binding.tvTimeTableOrder.text = getString(
                            R.string.timetable_order,
                            position + 1,
                            viewModel.getScheduleSize()
                        )
                    }
                    SharedPrefs.lastTimeTableIndex = position
                }
            })

            btnForward.setOnClickListener {
                if (viewPager.currentItem < viewModel.getScheduleSize()) {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                }
            }

            btnBackward.setOnClickListener {
                if (viewPager.currentItem > 0) {
                    viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                }
            }
        }
    }

    private fun saveTimetables() {
        viewModel.saveTimeTables()
    }

    /**
     * Create viewpager adapter contains the mutable collection of fragment representing the list of timetable
     */
    private fun createViewPagerAdapter(): RecyclerView.Adapter<*> {
        return object : FragmentStateAdapter(this) {
            val timetableViewModel = viewModel
            override fun createFragment(position: Int): JobScheduleFragment {
                val arraySchedule = timetableViewModel.getScheduleAt(position) ?: ScheduleWrapper()
                return JobScheduleFragment.newInstance(
                    arraySchedule,
                    listener = this@TimetableFragment
                )
            }

            override fun getItemCount(): Int =
                if (timetableViewModel.getScheduleSize() == 0) 1 else timetableViewModel.getScheduleSize()

            override fun getItemId(position: Int): Long =
                timetableViewModel.getScheduleId(position) ?: 0L

            override fun containsItem(itemId: Long): Boolean = timetableViewModel.contains(itemId)
        }
    }

    override fun onJobClick(jobId: String, isFavorite: Boolean) {
        parentFragmentManager.replace(
            JobDetailFragment.newInstance(jobId, isFavorite),
            container = R.id.host_fragment,
            allowAddToBackStack = true
        )
        shouldObserveData = false
    }

    private fun showSetTimeAvailableDialog() {
        SetTimeAvailableBottomSheet.newInstance().apply {
            onNewSchedule = { jobSchedule ->
                //viewModel.buildListJobSchedule(jobSchedule)
                buildTimetables(jobSchedule)
            }
        }.show(
            childFragmentManager,
            SetTimeAvailableBottomSheet::class.java.name
        )
    }

    private fun buildTimetables(jobSchedule: JobSchedules) {
        viewModel.buildListTimeTable(jobSchedule).observe(viewLifecycleOwner, {
            when (it.status) {
                ApiStatus.LOADING -> {
                    this@TimetableFragment.showLoadingDialog("Building job schedule", "")
                }
                ApiStatus.SUCCESS -> {
                    if (shouldObserveData) {
                        this@TimetableFragment.hideLoadingDialog()
                        this@TimetableFragment.showMessageDialog(
                            getString(R.string.create_schedule_completed),
                            getString(
                                R.string.create_schedule_completed_notice,
                                it.data!!.size,
                                it.data[0].salary.toCurrencyFormat()
                            ) + "Weekly"
                        )
                    } else {
                        shouldObserveData = true
                    }
                }
                ApiStatus.ERROR -> {
                    this@TimetableFragment.hideLoadingDialog()
                    this@TimetableFragment.showMessageDialog(
                        getString(R.string.encountered_error),
                        getString(R.string.create_schedule_failed_warning)
                    )
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timetable_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear_all -> {
                if(viewModel.getScheduleSize() != 0) {
                    viewModel.clearAllTimeTables()
                }
                true
            }
            R.id.menu_item_remove_others -> {
                if(viewModel.getScheduleSize() != 0) {
                    viewModel.removeOthers(binding.viewPager.currentItem)
                }
                true
            }
            R.id.menu_item_remove_this -> {
                if(viewModel.getScheduleSize() != 0) {
                    viewModel.removeTimetableAt(binding.viewPager.currentItem)
                }
                true
            }
            R.id.menu_item_timetable_info -> {
                if(viewModel.getScheduleSize() != 0) {
                    val scheduleWrapper = viewModel.getScheduleAt(binding.viewPager.currentItem)
                    this.showMessageDialog("Timetable Info", """
                    Create date: ${scheduleWrapper!!.createDate.toString("dd/MM/yyyy")}
                    
                    Total salary: ${scheduleWrapper.salary.toCurrencyFormat()} Weekly  
                """.trimIndent())
                }
                true
            }
            else -> false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimetableFragment()
    }

}