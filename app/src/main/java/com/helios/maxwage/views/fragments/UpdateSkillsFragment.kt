package com.helios.maxwage.views.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.helios.maxwage.R
import com.helios.maxwage.api.ApiStatus
import com.helios.maxwage.databinding.FragmentUpdateSkillsBinding
import com.helios.maxwage.viewmodels.UpdateSkillsViewModel
import com.helios.maxwage.views.base.BaseFragment

private const val ARG_LIST_SKILL = "listSkill"

class UpdateSkillsFragment : BaseFragment() {

    private lateinit var binding: FragmentUpdateSkillsBinding
    private val viewModel: UpdateSkillsViewModel by viewModels()

    private var listSkill: MutableList<String>? = null
    private val commonSkillPool: MutableList<String> by lazy {
        resources.getStringArray(R.array.skills).toMutableList().apply {
            listSkill?.forEach {
                this.remove(it)
            }
        }
    }

    override val TAG: String
        get() = "UpdateSkillFragment"
    override val shouldHideNavigationView: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            val listSkillJson = it.getString(ARG_LIST_SKILL)
            val type = object : TypeToken<MutableList<String>>() {}.type
            listSkill = Gson().fromJson(listSkillJson, type)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateSkillsBinding.inflate(inflater, container, false)

        initializeViewComponents()
        setupToolbar()

        return binding.root
    }

    private fun initializeViewComponents() {
        setupSelectedChipGroup()
        setupCommonSkillChipGroup()

        with(binding) {
            btnSave.setOnClickListener {
                viewModel.updateMySkills(listSkill!!).observe(viewLifecycleOwner, {
                    when (it.status) {
                        ApiStatus.LOADING -> {
                        }
                        ApiStatus.SUCCESS -> {
                            this@UpdateSkillsFragment.showMessageToast("${it.message}")
                            parentFragmentManager.popBackStack()
                        }
                        ApiStatus.ERROR -> {
                            this@UpdateSkillsFragment.showMessageToast("${it.message}")
                        }
                    }
                })
            }
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
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

    private fun setupSelectedChipGroup() {
        with(binding) {
            groupSelectedSkills.removeAllViews()
            listSkill?.forEach { skill ->
                val chip: Chip = Chip(requireContext()).apply {
                    text = skill
                    isCloseIconVisible = true
                    setOnCloseIconClickListener {
                        listSkill?.remove(skill)
                        groupSelectedSkills.removeView(this)

                        commonSkillPool.add(skill)
                        setupCommonSkillChipGroup()
                    }
                    isSelected = true
                }
                groupSelectedSkills.addView(chip)
            }
        }
    }

    private fun setupCommonSkillChipGroup() {
        with(binding) {
            groupCommonSkills.removeAllViews()
            commonSkillPool.forEach { skill ->
                val chip: Chip = Chip(requireContext()).apply {
                    text = skill
                    setOnClickListener {
                        if (listSkill!!.size < 10) {
                            commonSkillPool.remove(skill)
                            groupCommonSkills.removeView(this)

                            listSkill?.add(skill)
                            setupSelectedChipGroup()
                        } else {
                            this@UpdateSkillsFragment.showMessageToast("You can only add up to 10 skills")
                        }
                    }
                }
                groupCommonSkills.addView(chip)
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(listSkill: List<String>) =
            UpdateSkillsFragment().apply {
                arguments = Bundle().apply {
                    val listSkillJson = Gson().toJson(listSkill)
                    putString(ARG_LIST_SKILL, listSkillJson)
                }
            }
    }
}