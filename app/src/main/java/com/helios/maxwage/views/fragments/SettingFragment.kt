package com.helios.maxwage.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.helios.maxwage.BuildConfig
import com.helios.maxwage.R
import com.helios.maxwage.databinding.FragmentSettingBinding
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.replace
import com.helios.maxwage.views.activities.LoginActivity
import com.helios.maxwage.views.activities.MainActivity
import com.helios.maxwage.views.base.BaseFragment

class SettingFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingBinding
    override val TAG: String
        get() = "SettingFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingBinding.inflate(inflater, container, false)

        initializeViewComponents()

        return binding.root
    }

    private fun initializeViewComponents() {
        childFragmentManager.replace(SettingPreference(), container = R.id.framelayout_setting)
        (activity as MainActivity).hideFab()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }

    class SettingPreference : PreferenceFragmentCompat() {

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            listView.overScrollMode = View.OVER_SCROLL_NEVER
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings, rootKey)

            val onlyJobsMatchAddressPref =
                findPreference<SwitchPreferenceCompat>("onlyJobMatchAddress")
            val onlyJobsMatchSkillsPref =
                findPreference<SwitchPreferenceCompat>("onlyJobMatchSkills")

            findPreference<Preference>("version")?.apply {
                summary = BuildConfig.VERSION_NAME
            }

            onlyJobsMatchAddressPref?.apply {
                isChecked = SharedPrefs.onlyJobsMatchAddress
                setOnPreferenceClickListener {
                    it as SwitchPreferenceCompat
                    SharedPrefs.onlyJobsMatchAddress = it.isChecked
                    SharedPrefs.onlyJobsMatchSkills = it.isChecked.and(false)

                    onlyJobsMatchSkillsPref?.isChecked = SharedPrefs.onlyJobsMatchSkills

                    true
                }
            }

            onlyJobsMatchSkillsPref?.apply {
                isChecked = SharedPrefs.onlyJobsMatchSkills
                setOnPreferenceClickListener {
                    it as SwitchPreferenceCompat
                    SharedPrefs.onlyJobsMatchSkills = it.isChecked
                    SharedPrefs.onlyJobsMatchAddress = it.isChecked.and(false)

                    onlyJobsMatchAddressPref?.isChecked = SharedPrefs.onlyJobsMatchAddress
                    true
                }
            }

            findPreference<Preference>("logout")?.setOnPreferenceClickListener {
                SharedPrefs.accessToken = ""
                Intent(requireContext(), LoginActivity::class.java).apply {
                    startActivity(this)
                    activity?.finish()
                }
                true
            }
        }
    }
}