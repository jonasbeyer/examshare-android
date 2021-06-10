package de.twisted.examshare.ui.signin.find

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivityRequestPasswordResetBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.util.extensions.setUpErrorSnackbar
import de.twisted.examshare.util.extensions.setUpOverlayProgressBar
import de.twisted.examshare.util.result.EventObserver
import javax.inject.Inject

class RequestPasswordResetActivity : ExamActivity() {

    companion object {
        private const val DIALOG_REQUEST_PASSWORD_RESET = "dialog_request_password_reset"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<RequestPasswordResetViewModel>
    lateinit var binding: ActivityRequestPasswordResetBinding

    private val viewModel: RequestPasswordResetViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_password_reset)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.includeDefaultAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpOverlayProgressBar(viewModel.isLoading, this)
        setUpErrorSnackbar(viewModel.passwordResetError, binding.root, this)

        viewModel.navigateToConfirmationDialog.observe(this, EventObserver {
            showRequestPasswordResetConfirmationDialog()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showRequestPasswordResetConfirmationDialog() {
        val dialog = RequestPasswordResetConfirmDialog()
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, DIALOG_REQUEST_PASSWORD_RESET)
    }
}