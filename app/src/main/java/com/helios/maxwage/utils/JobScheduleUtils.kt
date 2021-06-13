package com.helios.maxwage.utils

import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.helios.maxwage.models.JobSchedule

object JobScheduleUtils {

    fun buildFreeTimeArgument(arrayHour: Array<Boolean>): Array<MutableList<String>> {
        val freeTime: Array<MutableList<String>> = Array(7) { mutableListOf<String>() }
        var len = 0
        arrayHour.forEachIndexed { index, value ->
            if (value) {
                len++
            } else {
                if (len > 0) {
                    val rightIndex = (index - 1) % 24
                    val leftIndex = (index - len) % 24
                    val time = "${leftIndex.toTimeFormat()}-${(rightIndex + 1).toTimeFormat()}"
                    freeTime[(index - 1) / 24].add(time)
                }
                len = 0
            }

            val nextIndex = index + 1
            if (nextIndex % 24 == 0) {
                if (len > 0) {
                    val rightIndex = (index + 1) % 24
                    val leftIndex = (index - len + 1) % 24
                    val time = "${leftIndex.toTimeFormat()}-${rightIndex.toTimeFormat()}"
                    freeTime[index / 24].add(time)
                }
                len = 0
            }
        }
        return freeTime
    }

    fun mapJobScheduleToSchedules(jobSchedule: JobSchedule): ArrayList<Schedule> {
        val schedules = ArrayList<Schedule>()

        jobSchedule.combo.forEachIndexed { index, value ->
            if (value.isNotEmpty()) {
                value.forEach {
                    val schedule = Schedule().apply {
                        val startTimeStr = it.time.split("-")[0]
                        val endTimeStr = it.time.split("-")[1]
                        val startTimeHour = startTimeStr.split(":")[0].toInt()
                        val startTimeMinute = startTimeStr.split(":")[1].toInt()
                        val endTimeHour = endTimeStr.split(":")[0].toInt()
                        val endTimeMinute = endTimeStr.split(":")[1].toInt()

                        day = index
                        jobId = it.jobIds[0]
                        jobTitle = it.jobIds[0]
                        startTime = Time(startTimeHour, startTimeMinute)
                        endTime = Time(endTimeHour, endTimeMinute)
                    }

                    schedules.add(schedule)
                }

            }
        }

        return schedules
    }
}