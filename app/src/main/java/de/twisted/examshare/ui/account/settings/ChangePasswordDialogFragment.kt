package de.twisted.examshare.ui.account.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import de.twisted.examshare.R
import de.twisted.examshare.ui.account.AccountViewModel
import de.twisted.examshare.ui.shared.AsyncDialogBuilder
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.BaseDialogFragment
import javax.inject.Inject

class ChangePasswordDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<AccountViewModel>

    private val viewModel: AccountViewModel by activityViewModels { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_update_password, null)

        return AsyncDialogBuilder(requireContext())
                .setPositiveButtonAsync(R.string.ok) {}
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.define_new_password)
                .setView(view)
                .create()
    }
}