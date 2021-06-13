package com.helios.maxwage.epoxycontroller

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.helios.maxwage.models.epoxy.WeekDayHeader
import com.helios.maxwage.models.epoxy.freeHourRow
import com.helios.maxwage.models.epoxy.weekDayHeader

/**
 * Created by Helios on 6/9/2021.
 */

class SetFreeTimeController : EpoxyController() {

    private var days = mutableListOf<WeekDayHeader>()

    var addNewHour: ((dayIndex: Int) -> Unit)? = null

    var onDeleteHour: ((day: Int, startHour: Int, endHour: Int) -> Unit)? = null

    fun setData(items: List<WeekDayHeader>) {
        days.clear()
        days.addAll(items)

        requestModelBuild()
    }

    override fun buildModels() {
        days.forEach { day ->
            if (day.shouldShow) {
                weekDayHeader {
                    id(day.title)
                    title(day.title)
                    hourAmount(day.hours.size)
                    expanded(day.expanded)
                    onClick(View.OnClickListener {
                        if (day.hours.isNotEmpty()) {
                            day.expanded = !day.expanded
                            requestModelBuild()
                        }
                    })
                    addNewHour(View.OnClickListener {
                        addNewHour?.invoke(day.dayIndex)
                    })
                }
            }
            if (day.expanded) {
                day.hours.forEach { hour ->
                    freeHourRow {
                        id(hour)
                        title(hour)
                        onDelete(View.OnClickListener {
                            val startTime = hour.split("-")[0].split(":")[0].toInt()
                            var endTime = hour.split("-")[1].split(":")[0].toInt()
                            if (endTime == 0) {
                                endTime = 23
                            }
                            onDeleteHour?.invoke(day.dayIndex, startTime, endTime)
                        })
                    }
                }
            }
        }
    }
}