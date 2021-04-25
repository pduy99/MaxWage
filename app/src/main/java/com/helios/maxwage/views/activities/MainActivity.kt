package com.helios.maxwage.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ActivityMainBinding
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.fragment.replace
import com.helios.maxwage.views.bottomsheet.SetTimeAvailableBottomSheet
import com.helios.maxwage.views.fragments.AccountFragment
import com.helios.maxwage.views.fragments.JobFragment
import com.helios.maxwage.views.fragments.SettingFragment
import com.helios.maxwage.views.fragments.TimetableFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initializeViewComponents()

        if(SharedPrefs.selected_fragment == null){
            val item = binding.bottomNavigation.menu.getItem(0)
            onNavigationItemSelected(item)
        }else{
            val item = binding.bottomNavigation.menu.findItem(SharedPrefs.selected_fragment!!)
            onNavigationItemSelected(item)
        }
    }

    private fun initializeViewComponents() {
        with(binding){
            // bottom navigation
            bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)

            bottomNavigation.setOnNavigationItemReselectedListener {
                // Nothing
            }

            //fab
            fabNewSuggestion.setOnClickListener {
                SetTimeAvailableBottomSheet.newInstance().show(
                        supportFragmentManager,
                        SetTimeAvailableBottomSheet::class.java.name
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        SharedPrefs.selected_fragment = binding.bottomNavigation.selectedItemId
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.bottomNavigation.menu.findItem(item.itemId).isChecked = true
        return when(item.itemId){
            R.id.menu_item_timetable -> {
                val fragment = TimetableFragment.newInstance()
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            R.id.menu_item_jobs -> {
                val fragment = JobFragment.newInstance("", "")
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            R.id.menu_item_account -> {
                val fragment = AccountFragment.newInstance("", "")
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            R.id.menu_item_setting -> {
                val fragment = SettingFragment.newInstance("", "")
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            else -> false
        }
    }

}