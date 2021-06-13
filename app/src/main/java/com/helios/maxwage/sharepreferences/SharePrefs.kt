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

    private val ACCESS_TOKEN = Pair("access_token", String)
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

    var accessToken: String?
        get() {
            return preferences.getString(ACCESS_TOKEN.first, null)
        }
        set(value) {
            preferences.edit {
                it.putString(ACCESS_TOKEN.first, value)
            }
        }

    var selectedFragment: Int?
        get() {
            return preferences.getInt(SELECTED_FRAGMENT.first, 0)
        }
        set(value) {
            preferences.edit {
                it.putInt(SELECTED_FRAGMENT.first, value!!)
            }
        }
}
