package com.helios.maxwage.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.helios.maxwage.R

/**
 * Created by Helios on 6/5/2021.
 */

object UserNotificationHelper {

    fun showMessageDialog(
        context: Context,
        title: String,
        message: String,
        actionOnDone: () -> Unit = {}
    ) {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                actionOnDone.invoke()
            }
        }
        if (context is AppCompatActivity) {
            context.runOnUiThread {
                builder.create().show()
            }
        } else {
            builder.create().show()
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
        val builder = AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(cancelable)
            setPositiveButton(positiveText) { _, _ ->
                actionOnAgree.invoke()
            }
            setNegativeButton(negativeText) { _, _ ->
                actionOnCancel.invoke()
            }
        }
        if (context is AppCompatActivity) {
            context.runOnUiThread {
                builder.create().show()
            }
        } else {
            builder.create().show()
        }
    }

    fun showToastMessage(context: Context, message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showLoadingDialog(
        context: Context,
        title: String,
        message: String,
        cancelable: Boolean,
        actionOnCancel: () -> Unit = {}
    ): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setView(R.layout.layout_loading)
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            if (cancelable) {
                setNegativeButton("CANCEL") { _, _ ->
                    actionOnCancel.invoke()
                }
            }
        }

        val dialog = builder.create().apply {
            if (title.isBlank()) {
                this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            }
        }
        dialog.show()

        timerDelayRemoveDialog(10_000, dialog)

        return dialog
    }

    private fun timerDelayRemoveDialog(time: Long, d: Dialog) {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (d.isShowing) {
                    d.dismiss()
                }
            }, time
        )
    }
}