package com.helios.maxwage.views.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.helios.maxwage.utils.UserNotificationHelper
import com.helios.maxwage.views.activities.MainActivity

/**
 * Created by Helios on 6/4/2021.
 */

abstract class BaseFragment : Fragment() {
    private var loadingDialog: AlertDialog? = null

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            if (shouldHideNavigationView) {
                (activity as MainActivity).hideNavigationView()
            } else {
                (activity as MainActivity).showNavigationView()
            }
        }
    }

    fun showLoadingDialog(title: String, message: String, actionOnCancel: () -> Unit = {}) {
        runOnUIThread {
            loadingDialog?.apply {
                if (title.isNotEmpty()) {
                    setTitle(title)
                }
                if (message.isNotEmpty()) {
                    setMessage(message)
                }
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

    fun hideLoadingDialog() {
        runOnUIThread {
            loadingDialog?.dismiss()
        }

        loadingDialog = null
    }

    fun showMessageDialog(title: String, message: String, actionOnDone: () -> Unit = {}) {
        activity?.run {
            UserNotificationHelper.showMessageDialog(this, title, message, actionOnDone)
        }
    }

    fun showConfirmDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String = "OK",
        negativeText: String = "CANCEL",
        actionOnCancel: () -> Unit = {},
        actionOnAgree: () -> Unit = {},
        cancelable: Boolean = false
    ) {
        activity?.run {
            UserNotificationHelper.showConfirmDialog(
                context,
                title,
                message,
                positiveText,
                negativeText,
                actionOnCancel,
                actionOnAgree,
                cancelable
            )
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
    protected open val shouldHideNavigationView: Boolean = false
}