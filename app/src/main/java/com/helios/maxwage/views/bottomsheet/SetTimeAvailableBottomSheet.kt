package com.helios.maxwage.views.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.LayoutSetTimeAvailabilityBinding
import com.helios.maxwage.interfaces.IButtonScheduleClick
import com.helios.maxwage.models.JobSchedule
import com.helios.maxwage.utils.fragment.replace
import com.helios.maxwage.viewmodels.SetTimeAvailableViewModel
import com.helios.maxwage.views.base.BaseBottomSheetFragment

/**
 * Created by Helios on 3/17/2021.
 */

class SetTimeAvailableBottomSheet private constructor() : BaseBottomSheetFragment(),
    IButtonScheduleClick {

    private lateinit var binding: LayoutSetTimeAvailabilityBinding
    private val viewModel: SetTimeAvailableViewModel by viewModels()
    private val freeTimeFragment: SetFreeTimeFragment by lazy {
        SetFreeTimeFragment.newInstance(this)
    }
    private val workShiftFragment: SetFreeWorkShiftFragment by lazy {
        SetFreeWorkShiftFragment.newInstance(this)
    }

    var onNewSchedule: ((JobSchedule) -> Unit)? = null

    override val TAG: String
        get() = "AvailableTimeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSetTimeAvailabilityBinding.inflate(inflater, container, false)

        childFragmentManager.replace(workShiftFragment, container = R.id.frameLayoutContent)
        initData()
        initializeViewComponents()

        return binding.root
    }

    private fun initData() {

    }

    private fun initializeViewComponents() {
        with(binding) {
            segmentGroupFreeTime.setOnPositionChangedListener { newPosition ->
                if (newPosition == 0) {
                    childFragmentManager.replace(
                        workShiftFragment,
                        container = R.id.frameLayoutContent,
                        allowAddToBackStack = true
                    )
                } else if (newPosition == 1) {
                    childFragmentManager.replace(
                        freeTimeFragment,
                        container = R.id.frameLayoutContent,
                        allowAddToBackStack = true
                    )
                }
            }
        }
    }


    companion object {
        fun newInstance(): SetTimeAvailableBottomSheet {
            return SetTimeAvailableBottomSheet()
        }
    }

    override fun onButtonScheduleClick(selectedHour: Array<Boolean>, isOnlyFavoriteJobs: Boolean) {
        val freeTimeArgument = viewModel.buildFreeTimeArgument(selectedHour)

        viewModel.scheduleJob(freeTimeArgument, isOnlyFavoriteJobs)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ApiStatus.LOADING -> {
                        this@SetTimeAvailableBottomSheet.showLoadingDialog(
                            "Scheduling",
                            requireContext().getString(R.string.scheduling_dialog_message)
                        )
                    }
                    ApiStatus.SUCCESS -> {
                        this@SetTimeAvailableBottomSheet.hideLoadingDialog()

                        if (it.data!!.combo.isEmpty()) {
                            this@SetTimeAvailableBottomSheet.showMessageDialog(
                                "No Job Schedule Found",
                                "Sorry, we could not find any jobs matching your conditions."
                            )
                        } else {
                            onNewSchedule?.invoke(it.data)
                            this@SetTimeAvailableBottomSheet.dismiss()
                        }
                    }
                    ApiStatus.ERROR -> {
                        this@SetTimeAvailableBottomSheet.hideLoadingDialog()
                        this@SetTimeAvailableBottomSheet.showMessageToast(it.message!!)
                        Log.d(TAG, "${it.message}")
                    }
                }
            })
    }

}