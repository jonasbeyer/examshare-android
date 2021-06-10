package de.twisted.examshare.ui.signup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.DataBindingUtil
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivitySignUpBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.util.extensions.setUpErrorSnackbar
import de.twisted.examshare.util.extensions.setUpOverlayProgressBar
import de.twisted.examshare.util.result.EventObserver
import javax.inject.Inject

class SignUpActivity : ExamActivity() {

    companion object {
        private const val DIALOG_SIGNUP_COFIRMATION = "dialog_signup_confirmation"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SignUpViewModel>
    lateinit var binding: ActivitySignUpBinding

    private val viewModel: SignUpViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        setSupportActionBar(binding.includeDefaultAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prepareCheckboes()

        viewModel.navigateToConfirmationDialog.observe(this, EventObserver {
            showSignupConfirmationDialog()
        })

        setUpOverlayProgressBar(viewModel.isLoading, this)
        setUpErrorSnackbar(viewModel.signUpError, binding.root, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun prepareCheckboes() {
        binding.ageRestriction.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.ageRestriction.error = null
            }
        }

        binding.acceptPrivacyPolicy.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = HtmlCompat.fromHtml(
                getString(R.string.accept_privacy_policy),
                FROM_HTML_MODE_LEGACY
            )

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.acceptPrivacyPolicy.error = null
                }
            }
        }
    }

    private fun showSignupConfirmationDialog() {
        val dialog = SignUpConfirmationDialog().apply { isCancelable = false }
        dialog.show(supportFragmentManager, DIALOG_SIGNUP_COFIRMATION)
    }

    private fun validateCheckBoxes(vararg checkBoxes: CheckBox): Boolean {
        var valid = true
        for (checkBox in checkBoxes) {
            if (!checkBox.isChecked) {
                checkBox.error = ""
                valid = false
            }
        }
        return valid
    }
}