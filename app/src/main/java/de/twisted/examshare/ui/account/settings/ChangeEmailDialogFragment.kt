package de.twisted.examshare.ui.account.settings

import android.app.Dialog
import android.os.Bundle
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.AsyncDialogBuilder
import de.twisted.examshare.ui.shared.base.BaseDialogFragment

class ChangeEmailDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = layoutInflater.inflate(R.layout.dialog_generic_input, null)

        return AsyncDialogBuilder(requireContext())
                .setPositiveButtonAsync(R.string.send) {}
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.enter_new_email)
                .setView(layout)
                .create()
    }
}