package com.helios.maxwage.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.*

/**
 * Created by Helios on 6/15/2021.
 */
class BindingAdapters {

    companion object {

        @JvmStatic
        @BindingAdapter("jobName")
        fun standardizeJobName(view: TextView, jobName: String?) {
            val standardizedName = jobName?.standardizeCase(Locale.getDefault())
            view.text = standardizedName
        }

        @JvmStatic
        @BindingAdapter("jobWage")
        fun jobWageStr(view: TextView, wage: Int?) {
            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("VND")
            val wageStr = format.format(wage) + " Hourly"

            view.text = wageStr
        }

        /**
         * Build the string from list skills with format
         * """
         *  \t - skill 1
         *  \t - skill 2
         *  ...
         * """
         */
        @JvmStatic
        @BindingAdapter("jobSkills")
        fun jobSkillsStr(view: TextView, skills: List<String>?) {
            var jobSkillsStr = ""
            skills?.forEach {
                jobSkillsStr += "\t - ${it.standardizeCase(Locale.getDefault())} \n\n"
            }

            view.text = jobSkillsStr
        }
    }
}