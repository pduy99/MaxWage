package com.helios.maxwage.views.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.helios.maxwage.R
import com.helios.maxwage.databinding.LayoutSetTimeAvailabilityBinding
import com.helios.maxwage.databinding.LayoutTimeAvailabilityBinding

/**
 * Created by Helios on 3/17/2021.
 */

class SetTimeAvailableBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : LayoutSetTimeAvailabilityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MaterialComponents_BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.setOnShowListener {
                (dialog as BottomSheetDialog).let { bottomSheetDialog ->
                    val bottomSheet: FrameLayout =
                            bottomSheetDialog.findViewById(R.id.design_bottom_sheet)!!
                    val behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior.apply {
                        state = BottomSheetBehavior.STATE_EXPANDED
                        skipCollapsed = true
                    }
                    behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                //In the EXPANDED STATE apply a new MaterialShapeDrawable with rounded corner
                                val newMaterialShapeDrawable: MaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
                                ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutSetTimeAvailabilityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel =  //Create a ShapeAppearanceModel with the same shapeAppearanceOverlay used in the style
                ShapeAppearanceModel.builder(context, 0, R.style.CustomShapeAppearanceBottomSheetDialog)
                        .build()

        //Create a new MaterialShapeDrawable (you can't use the original MaterialShapeDrawable in the BottomSheet)
        val currentMaterialShapeDrawable = bottomSheet.background as MaterialShapeDrawable
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        //Copy the attributes in the new MaterialShapeDrawable
        newMaterialShapeDrawable.initializeElevationOverlay(context)
        newMaterialShapeDrawable.fillColor = currentMaterialShapeDrawable.fillColor
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
    }

    fun onTimeToggle(view: View){
        view as TextView
        view.isSelected = !view.isSelected
    }

    companion object {
        fun newInstance(): SetTimeAvailableBottomSheet {
            return SetTimeAvailableBottomSheet()
        }

        const val TAG = "SetTimeAvailableBottomSheet"
    }

}