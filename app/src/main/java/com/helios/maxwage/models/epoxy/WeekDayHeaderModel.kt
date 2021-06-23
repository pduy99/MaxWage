package com.helios.maxwage.models.epoxy

import android.view.View
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.helios.maxwage.R
import com.helios.maxwage.databinding.LayoutWeekDayHeaderBinding
import com.helios.maxwage.utils.ViewBindingEpoxyModelWithHolder

/**
 * Created by Helios on 6/9/2021.
 */

@EpoxyModelClass(layout = R.layout.layout_week_day_header)
abstract class WeekDayHeaderModel : ViewBindingEpoxyModelWithHolder<LayoutWeekDayHeaderBinding>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    var hourAmount: Int = 0

    @EpoxyAttribute
    var expanded: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClick: View.OnClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var addNewHour: View.OnClickListener? = null

    override fun LayoutWeekDayHeaderBinding.bind() {
        tvHeaderTitle.text = title
        tvCount.text = hourAmount.toString()
        containerRoot.setOnClickListener(onClick)
        ivAddNewTask.setOnClickListener(addNewHour)

        if (hourAmount == 0) {
            tvCount.isVisible = false
            ivAddNewTask.isVisible = true
        } else {
            ivAddNewTask.isVisible = expanded
            tvCount.isVisible = !expanded
        }
    }
}