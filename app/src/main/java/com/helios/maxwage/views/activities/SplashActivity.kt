package com.helios.maxwage.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.helios.maxwage.sharepreferences.SharedPrefs
import com.helios.maxwage.utils.JWTUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        if (!checkTokenExpired()) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        } else {
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    private fun checkTokenExpired(): Boolean {
        SharedPrefs.accessToken?.let {
            return JWTUtils.checkExpired(it)
        }
        return false
    }
}