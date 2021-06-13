package com.helios.maxwage.views.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.helios.maxwage.R
import com.helios.maxwage.utils.UserNotificationHelper

/**
 * Created by Helios on 6/4/2021.
 */

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MaterialComponents_BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.setOnShowListener {
                (dialog as BottomSheetDialog).let { bottomSheetDialog ->
                    val offsetFromTop = 400
                    val bottomSheet: FrameLayout =
                        bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                    val behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior.apply {
                        expandedOffset = offsetFromTop
                        state = BottomSheetBehavior.STATE_EXPANDED
                        skipCollapsed = true
                        isFitToContents = true
                        isDraggable = false
                    }

                    behavior.addBottomSheetCallback(object :
                        BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                //In the EXPANDED STATE apply a new MaterialShapeDrawable with rounded corner
                                val newMaterialShapeDrawable: MaterialShapeDrawable =
                                    createMaterialShapeDrawable(bottomSheet)
                                ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {

                        }
                    })
                }
            }
        }
    }

    fun showLoadingDialog(title: String, message: String, actionOnCancel: () -> Unit = {}) {
        runOnUIThread {
            loadingDialog?.apply {
                setTitle(title)
                setMessage(message)
                setOnCancelListener { actionOnCancel.invoke() }

                return@runOnUIThread
            }

            activity?.let {
                loadingDialog?.dismiss()
                loadingDialog = UserNotificationHelper.showLoadingDialog(
                    it,
                    title,
                    message,
                    false,
                    actionOnCancel
                )
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel =
            //Create a ShapeAppearanceModel with the same shapeAppearanceOverlay used in the style
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

    fun hideLoadingDialog() {
        runOnUIThread {
            loadingDialog?.dismiss()
        }

        loadingDialog = null
    }

    fun showMessageDialog(title: String, message: String) {
        activity?.run {
            UserNotificationHelper.showMessageDialog(this, title, message)
        }
    }

    fun showMessageToast(message: String) {
        runOnUIThread {
            UserNotificationHelper.showToastMessage(requireContext(), message)
        }
    }

    protected fun runOnUIThread(action: () -> Unit) {
        activity?.runOnUiThread {
            try {
                action.invoke()
            } catch (ex: Exception) {
                // do nothing
            }
        }
    }

    abstract val TAG: String
}