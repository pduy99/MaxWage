package com.helios.maxwage.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.helios.maxwage.R
import com.helios.maxwage.databinding.LayoutJobFilterBinding
import com.helios.maxwage.utils.toCurrencyFormat
import com.helios.maxwage.views.base.BaseBottomSheetFragment

/**
 * Created by Helios on 6/19/2021.
 */
class JobFilterBottomSheet private constructor() : BaseBottomSheetFragment() {

    private lateinit var binding: LayoutJobFilterBinding

    private var selectedDistrict: MutableList<String> = mutableListOf()
    private var minWage: Int = 0
    private var matchMySkillsOnly: Boolean = false
    private var myFavoriteJobsOnly: Boolean = false

    var onApplyFilter: ((
        selectedDistrict: List<String>,
        minWage: Int,
        selectedSkills: List<String>,
        matchMySkillsOnly: Boolean,
        myFavoriteJobsOnly: Boolean
    ) -> Unit)? = null

    override val TAG: String
        get() = "JobFilter"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LayoutJobFilterBinding.inflate(inflater, container, false)

        initializeViewComponents()
        setupToolbar()

        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener {
            this@JobFilterBottomSheet.dismiss()
        }
    }

    private fun setupJobRequirementsChipGroup(showAll: Boolean) {
        binding.groupSkills.removeAllViews()
        val requirementPool = resources.getStringArray(R.array.skills).toMutableList()

        if (!showAll) {
            val temp = requirementPool.take(10)
            requirementPool.clear()
            requirementPool.addAll(temp)
        }

        requirementPool.forEach { requirement ->
            val chip = layoutInflater.inflate(R.layout.layout_choice_chip, null, false) as Chip
            chip.text = requirement
            binding.groupSkills.addView(chip)
        }
    }

    private fun setupDistrictAdapter() {
        val districtAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.HCMC_District)
        )
        binding.edDistrict.setAdapter(districtAdapter)
    }

    private fun initializeViewComponents() {
        setupJobRequirementsChipGroup(false)
        setupDistrictAdapter()

        with(binding) {

            toggleViewAllRequirement.setOnClickListener {
                it as TextView
                if (it.text == getString(R.string.view_all)) {
                    setupJobRequirementsChipGroup(true)
                    it.setText(R.string.view_less)
                } else if (it.text == getString(R.string.view_less)) {
                    setupJobRequirementsChipGroup(false)
                    it.setText(R.string.view_all)
                }
            }

            switchOnlyMySkills.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    groupSkills.visibility = View.GONE
                    toggleViewAllRequirement.visibility = View.GONE
                } else {
                    groupSkills.visibility = View.VISIBLE
                    toggleViewAllRequirement.visibility = View.VISIBLE
                }
                matchMySkillsOnly = isChecked
            }

            edDistrict.setOnItemClickListener { _, view, _, _ ->
                view as TextView
                edDistrict.setText("")
                if (!selectedDistrict.contains(view.text.toString())) {
                    addSelectedDistrict(view.text.toString())
                }
            }

            sliderMinWage.addOnChangeListener { _, value, _ ->
                minWage = value.toInt()
                tvMinWage.text = value.toCurrencyFormat()
            }

            switchOnlyFavoriteJob.setOnCheckedChangeListener { _, isChecked ->
                myFavoriteJobsOnly = isChecked
            }

            btnApply.setOnClickListener {
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val selectedSkills = mutableListOf<String>()
        if (binding.groupSkills.visibility != View.GONE) {
            val childrenCount = binding.groupSkills.childCount
            for (i in 0 until childrenCount) {
                val childAtIndex = binding.groupSkills.getChildAt(i) as Chip
                if (childAtIndex.isChecked) {
                    selectedSkills.add(childAtIndex.text.toString())
                }
            }
        }

        onApplyFilter?.invoke(
            selectedDistrict,
            minWage,
            selectedSkills,
            matchMySkillsOnly,
            myFavoriteJobsOnly
        )

        this@JobFilterBottomSheet.dismiss()
    }

    private fun addSelectedDistrict(district: String) {
        selectedDistrict.add(district)
        val chip = Chip(requireContext())
        chip.text = district
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            selectedDistrict.remove(district)
            binding.groupSelectedDistrict.removeView(chip)
        }
        binding.groupSelectedDistrict.addView(chip)
    }

    companion object {
        private var instance: JobFilterBottomSheet? = null

        fun getInstance() = instance ?: JobFilterBottomSheet().also {
            instance = it
        }
    }
}