package com.helios.maxwage.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ActivityMainBinding
import com.helios.maxwage.utils.fragment.replace
import com.helios.maxwage.views.fragments.AccountFragment
import com.helios.maxwage.views.fragments.JobFragment
import com.helios.maxwage.views.fragments.SettingFragment
import com.helios.maxwage.views.fragments.TimetableFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initializeViewComponents()
    }

    private fun initializeViewComponents() {
        with(binding){
            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_item_timetable -> {
                        val fragment = TimetableFragment.newInstance("", "")
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
            bottomNavigation.setOnNavigationItemReselectedListener {
                // Nothing
            }
        }
    }

}