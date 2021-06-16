package com.helios.maxwage.models

/**
 * Created by Helios on 6/16/2021.
 */
enum class DayInWeek(val shortName: String, val index: Int) {
    MONDAY("Mon", 0),
    TUESDAY("Tue", 1),
    WEDNESDAY("Wed", 2),
    THURSDAY("Thu", 3),
    FRIDAY("Fri", 4),
    SATURDAY("Sat", 5),
    SUNDAY("Sun", 6);

    companion object {
        fun fromIndex(value: Int) = values().first { it.index == value }
    }
}