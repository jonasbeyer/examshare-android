package de.twisted.examshare.ui.account.signout

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import de.twisted.examshare.R
import de.twisted.examshare.ui.account.AccountViewModel
import de.twisted.examshare.ui.shared.AsyncDialogBuilder
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.BaseDialogFragment
import de.twisted.examshare.util.ActivityHolder.logout
import de.twisted.examshare.util.result.EventUnhandledContentListener
import de.twisted.examshare.util.result.Result
import de.twisted.examshare.util.result.observeEvent
import javax.inject.Inject

class SignOutDialogFragment : BaseDialogFragment(), EventUnhandledContentListener<Result<Unit>> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<AccountViewModel>

    private val viewModel: AccountViewModel by activityViewModels { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AsyncDialogBuilder(requireContext())
                .setPositiveButtonAsync(R.string.logout) { viewModel.signOut() }
                .setTitle(R.string.logout)
                .setMessage(R.string.confirm_logout)
                .setNegativeButton(R.string.cancel, null)
                .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.accountUpdateLoading.observe(this, Observer { isLoading ->
            setDialogFragmentEnabled(!isLoading)
        })

        viewModel.accountUpdateResult.observeEvent(this, this)
    }

    override fun onEventUnhandledContent(value: Result<Unit>) {
        when (value) {
            is Result.Success -> {
                dismiss()
                logout(requireContext())
            }
            is Result.Error -> {
                Toast.makeText(requireContext(), R.string.connection_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}