package com.helios.maxwage

import android.app.Application
import android.content.Context
import com.helios.maxwage.sharepreferences.SharedPrefs

/**
 * Created by Helios on 3/17/2021.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeSharePrefs()
        initApplicationContext()
    }

    private fun initApplicationContext() {
        appContext = applicationContext
    }

    private fun initializeSharePrefs() {
        with(applicationContext) {
            SharedPrefs.init(this)
        }
    }

    companion object{
        lateinit var appContext: Context
    }
}