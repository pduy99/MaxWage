package com.helios.maxwage.models.epoxy

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.helios.maxwage.R
import com.helios.maxwage.databinding.LayoutFreeHourRowBinding
import com.helios.maxwage.utils.ViewBindingEpoxyModelWithHolder

/**
 * Created by Helios on 6/9/2021.
 */

@EpoxyModelClass(layout = R.layout.layout_free_hour_row)
abstract class FreeHourRowModel : ViewBindingEpoxyModelWithHolder<LayoutFreeHourRowBinding>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onDelete: View.OnClickListener? = null

    override fun LayoutFreeHourRowBinding.bind() {
        tvHour.text = title
        btnDelete.setOnClickListener(onDelete)
    }
}