package com.helios.maxwage.views.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ActivityMainBinding
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.replace
import com.helios.maxwage.views.fragments.AccountFragment
import com.helios.maxwage.views.fragments.JobFragment
import com.helios.maxwage.views.fragments.SettingFragment
import com.helios.maxwage.views.fragments.TimetableFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    var onFabClicked: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initializeViewComponents()

        try {
            val item = binding.bottomNavigation.menu.findItem(SharedPrefs.selectedFragment!!)
            onNavigationItemSelected(item)

        } catch (ex: Exception) {
            val item = binding.bottomNavigation.menu.getItem(0)
            onNavigationItemSelected(item)
        }

    }

    private fun initializeViewComponents() {
        with(binding) {
            // bottom navigation
            bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)

            bottomNavigation.setOnNavigationItemReselectedListener {
                // Nothing
            }

            fabNewSuggestion.setOnClickListener {
                onFabClicked?.invoke()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        SharedPrefs.selectedFragment = binding.bottomNavigation.selectedItemId
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.bottomNavigation.menu.findItem(item.itemId).isChecked = true
        return when (item.itemId) {
            R.id.menu_item_timetable -> {
                val fragment = TimetableFragment.newInstance()
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            R.id.menu_item_jobs -> {
                val fragment = JobFragment.newInstance()
                supportFragmentManager.replace(fragment, container = R.id.host_fragment)

                true
            }

            R.id.menu_item_account -> {
                val fragment = AccountFragment.newInstance()
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

    fun hideNavigationView() {
        binding.bottomBar.visibility = View.GONE
    }

    fun showNavigationView() {
        binding.bottomBar.visibility = View.VISIBLE
    }

    fun hideFab() {
        binding.fabNewSuggestion.hide()
    }

    fun showFab() {
        binding.fabNewSuggestion.show()
    }
}