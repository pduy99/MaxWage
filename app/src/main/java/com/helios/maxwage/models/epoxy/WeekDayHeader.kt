package com.helios.maxwage.models.epoxy

/**
 * Created by Helios on 6/9/2021.
 */

data class WeekDayHeader(
    var dayIndex: Int, //Monday = 0,...
    var title: String,
    var expanded: Boolean,
    var hours: MutableList<String>,
    var shouldShow: Boolean,
)