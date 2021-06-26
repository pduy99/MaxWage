package com.helios.maxwage.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            Intent(this, AuthenticationActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    private fun checkTokenExpired(): Boolean {
        return JWTUtils.checkExpired()
    }
}