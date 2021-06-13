package com.helios.maxwage.utils.string

import android.util.Patterns

/**
 * Created by Helios on 4/27/2021.
 */
object StringUtils {

    fun String.isEmailFormat(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}