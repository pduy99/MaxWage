package com.helios.maxwage.sharepreferences

import android.content.Context
import android.content.SharedPreferences
import com.helios.maxwage.utils.Constants.SHARED_PREFERENCES_NAME

/**
 * Created by Helios on 3/17/2021.
 */

object SharedPrefs {
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val SELECTED_FRAGMENT = Pair("selected_fragment", Int)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    // Properties

    var selected_fragment: Int?
        get() {
            return preferences.getInt("selected_fragment", 0)
        }
        set(value) {
            preferences.edit {
                it.putInt("selected_fragment", value!!)
            }
        }
}
