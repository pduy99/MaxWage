package com.helios.maxwage.interfaces

/**
 * Created by Helios on 6/8/2021.
 */

interface IButtonScheduleClick {
    fun onButtonScheduleClick(selectedHour: Array<Boolean>, isOnlyFavoriteJobs: Boolean)
}