package com.helios.maxwage.models

import com.github.tlaabs.timetableview.Schedule
import java.util.*

/**
 * Created by Helios on 6/25/2021.
 */

/**
 *  Represent for a schedule in a week
 */
data class ScheduleWrapper(
    val listSchedule: ArrayList<Schedule> = arrayListOf(),
    val salary: Int = 0,
    val createDate: Date = Calendar.getInstance().time,
)

/**
 * This class is for saving
 */
data class UserTimeTables(
    val userId: String = "",
    val listScheduleWrapper: List<ScheduleWrapper> = listOf()
)