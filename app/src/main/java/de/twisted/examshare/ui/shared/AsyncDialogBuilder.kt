package de.twisted.examshare.ui.shared

import android.app.Dialog
import android.content.Context
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

class AsyncDialogBuilder(context: Context) : AlertDialog.Builder(context) {

    private var positiveButtonCallback: ((dialog: AlertDialog) -> Unit)? = null

    fun setPositiveButtonAsync(
        @StringRes textId: Int,
        listener: (dialog: AlertDialog) -> Unit
    ): AsyncDialogBuilder {
        setPositiveButton(textId, null)
        positiveButtonCallback = listener
        return this
    }

    override fun create(): AlertDialog {
        val alertDialog: AlertDialog = super.create()

        positiveButtonCallback?.let { listener ->
            alertDialog.setOnShowListener {
                val positiveButton: Button = alertDialog.getButton(Dialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener { listener(alertDialog) }
            }
        }

        return alertDialog
    }
}