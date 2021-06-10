package de.twisted.examshare.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivitySignInBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.main.MainActivity
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.signin.find.RequestPasswordResetActivity
import de.twisted.examshare.ui.signup.SignUpActivity
import de.twisted.examshare.util.extensions.setUpErrorSnackbar
import de.twisted.examshare.util.extensions.setUpOverlayProgressBar
import de.twisted.examshare.util.result.EventObserver
import javax.inject.Inject

class SignInActivity : ExamActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SignInViewModel>
    lateinit var binding: ActivitySignInBinding

    private val viewModel: SignInViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToMainActivity.observe(this, EventObserver {
            openMainActivity()
        })

        viewModel.navigateToSignUpAction.observe(this, EventObserver {
            openSignUpActivity()
        })

        viewModel.navigateToResetPasswordAction.observe(this, EventObserver {
            openPasswordResetActivity()
        })

        setUpOverlayProgressBar(viewModel.isLoading, this)
        setUpErrorSnackbar(viewModel.signinErrorMessage, binding.root, this)
    }

//         case PENDING_EMAIl:
//             getProgressDialog().dismiss();
//             Dialogs.showEmailDialog(SignInActivity.this, response, username.getText(), password.getText());
//             break;
//         default:
//             if (!response.isDenied())
//                 new DialogBuilder(SignInActivity.this, response.getMessage()).show();

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun openPasswordResetActivity() {
        val intent = Intent(this, RequestPasswordResetActivity::class.java)
        startActivity(intent)
    }
}