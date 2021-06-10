package de.twisted.examshare.ui.signup

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.twisted.examshare.R

class SignUpConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle(R.string.pending_verification)
                .setMessage(R.string.verify_email)
                .setPositiveButton(R.string.ok) { _, _ -> activity?.onBackPressed() }
                .create()
    }
}