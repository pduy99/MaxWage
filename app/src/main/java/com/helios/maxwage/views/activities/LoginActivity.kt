package com.helios.maxwage.views.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ActivityLoginBinding
import com.helios.maxwage.interfaces.ILoginCallback
import com.helios.maxwage.utils.replace
import com.helios.maxwage.views.base.BaseActivity
import com.helios.maxwage.views.fragments.LoginFragment
import com.helios.maxwage.views.fragments.RegisterFragment

class LoginActivity : BaseActivity(), ILoginCallback {

    private lateinit var binding: ActivityLoginBinding
    private var loginFragment = LoginFragment.newInstance(this)
    private var registerFragment = RegisterFragment.newInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.layout_content, loginFragment)
                .commit()
        }

        initializeViewComponents()
    }

    private fun initializeViewComponents() {

    }

    override fun showRegisterScreen() {
        changeCameraDistance()
        supportFragmentManager.replace(
            registerFragment,
            container = R.id.layout_content,
            intAnim = R.animator.card_flip_in,
            outAnim = R.animator.card_flip_out
        )
    }

    override fun showLoginScreen() {
        changeCameraDistance()
        supportFragmentManager.replace(
            loginFragment,
            container = R.id.layout_content,
            intAnim = R.animator.card_flip_in,
            outAnim = R.animator.card_flip_out
        )
    }

    /**
     * Change the camera distance to prevent Flipping Animation from crossing the “virtual line”
     * and disappears
     */
    private fun changeCameraDistance() {
        val distance = 10000
        val scale = resources.displayMetrics.density * distance

        loginFragment.view?.cameraDistance = scale
        registerFragment.view?.cameraDistance = scale
    }
}