package de.twisted.examshare.ui.shared.base

import android.widget.Button
import androidx.appcompat.app.AlertDialog
import dagger.android.support.DaggerDialogFragment

abstract class BaseDialogFragment : DaggerDialogFragment() {

    protected val alertDialog: AlertDialog?
        get() = dialog as? AlertDialog

    protected open fun setDialogFragmentEnabled(enabled: Boolean) {
        alertDialog?.let {
            val positiveButton: Button? = it.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton: Button? = it.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton?.isEnabled = enabled
            negativeButton?.isEnabled = enabled
            it.setCancelable(enabled)
        }
    }

    protected open fun setPositiveButtonEnabled(enabled: Boolean) {
        alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = enabled
    }
}