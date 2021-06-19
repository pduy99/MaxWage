package com.helios.maxwage.sharepreferences

import android.content.Context
import android.content.SharedPreferences
import com.github.tlaabs.timetableview.Schedule
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.helios.maxwage.utils.Constants.SHARED_PREFERENCES_NAME

/**
 * Created by Helios on 3/17/2021.
 */

object SharedPrefs {
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val ACCESS_TOKEN = Pair("access_token", String)
    private val SELECTED_FRAGMENT = Pair("selected_fragment", Int)
    private val SAVED_JOB_SCHEDULE = Pair("saved_job_schedule", arrayListOf<Schedule>())
    private val ONLY_JOBS_MATCH_ADDRESS = Pair("jobs_match_address", Boolean)
    private val ONLY_JOBS_MATCH_SKILLS = Pair("jobs_match_skills", Boolean)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    // Properties

    var accessToken: String
        get() {
            return preferences.getString(ACCESS_TOKEN.first, "") ?: ""
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

    var savedJobSchedule: ArrayList<Schedule>?
        get() {
            return try {
                val jsonString = preferences.getString(SAVED_JOB_SCHEDULE.first, null)
                val turnsType = object : TypeToken<MutableList<Schedule>>() {}.type
                GsonBuilder().create().fromJson(jsonString, turnsType)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
        set(value) {
            val jsonString = GsonBuilder().create().toJson(value)
            preferences.edit {
                it.putString(SAVED_JOB_SCHEDULE.first, jsonString)
            }
        }

    var onlyJobsMatchAddress: Boolean
        get() {
            return preferences.getBoolean(ONLY_JOBS_MATCH_ADDRESS.first, false)
        }
        set(value) {
            preferences.edit {
                it.putBoolean(ONLY_JOBS_MATCH_ADDRESS.first, value)
            }
        }

    var onlyJobsMatchSkills: Boolean
        get() {
            return preferences.getBoolean(ONLY_JOBS_MATCH_SKILLS.first, false)
        }
        set(value) {
            preferences.edit {
                it.putBoolean(ONLY_JOBS_MATCH_SKILLS.first, value)
            }
        }

}
