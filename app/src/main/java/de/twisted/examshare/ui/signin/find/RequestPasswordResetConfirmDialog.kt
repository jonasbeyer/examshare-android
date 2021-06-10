package de.twisted.examshare.ui.signin.find

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.twisted.examshare.R

class RequestPasswordResetConfirmDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle(R.string.reset_password)
                .setMessage(R.string.reset_password_confirmation)
                .setPositiveButton(R.string.ok) { _, _ -> activity?.onBackPressed() }
                .create()
    }
}