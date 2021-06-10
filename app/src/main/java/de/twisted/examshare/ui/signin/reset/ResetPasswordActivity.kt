package de.twisted.examshare.ui.signin.reset

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivityResetPasswordBinding
import de.twisted.examshare.ui.main.MainActivity
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.signin.SignInActivity
import javax.inject.Inject

class ResetPasswordActivity : ExamActivity() {
// private RestAuthDataSource authManager;
// private ExamProgressBar progressBar;
//
// private EditText password;
// private TextView passwordError;
// private EditText passwordRepeat;
// private TextView passwordRepeatError;
//

    private var paused = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ResetPasswordViewModel>

    private lateinit var binding: ActivityResetPasswordBinding

    private val viewModel: ResetPasswordViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ExamShare)
        setTaskDescription()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = getUri()
        if (data == null || data.pathSegments.size != 3)
            return

        val tokenId = data.pathSegments[2]
        val userId = data.getQueryParameter("userId") ?: ""

        viewModel.setTokenValue(tokenId, userId)
        viewModel.tokenValiationResult.observe(this, Observer {

        })
    }

// @Override
// protected void onLoad(Bundle bundle) {
//     this.progressBar = findViewById(R.id.emptyProgressBar);
//     this.authManager = ExamShareApplication.getInstance().getAuthManager();
//
//     this.progressBar.setVisibility(View.VISIBLE);
//     this.authManager.verify(tokenId, userId, response -> {
//         this.progressBar.setVisibility(View.GONE);
//         if (response.getType() == ResponseType.ERROR) {
//             getSupportActionBar().setTitle(R.string.verification);
//             this.setupResponseLayout(getString(R.string.connection_error), false);
//             return;
//         }
//
//         if (response.getMessage().isEmpty()) {
//             getSupportActionBar().setTitle(R.string.reset_password);
//             this.setupPasswordView();
//         } else {
//             getSupportActionBar().setTitle(R.string.verification);
//             this.setupResponseLayout(response.getMessage(), response.getType() == ResponseType.SUCCESS);
//         }
//     });
// }
//

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        paused = false
        setIntent(intent)
        onCreate(null)
    }

    private fun getUri(): Uri? = if (intent.hasExtra("data")) {
        intent.getParcelableExtra("data")
    } else {
        intent.data
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onResume() {
        super.onResume()
        if (paused) {
            onBackPressed()
        }
    }

//
// @Override
// public void onBackPressed() {
//     if (!startNextActivity() && (paused || progressBar.getVisibility() != View.VISIBLE))
//         super.onBackPressed();
// }
//

    private fun startNextActivity(): Boolean {
        if (!isTaskRoot) {
            return false
        }

        val destination: Class<out ExamActivity> = if (viewModel.isSignedIn()) {
            MainActivity::class.java
        } else {
            SignInActivity::class.java
        }

        val intent = Intent(this, destination)
                .apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

        startActivity(intent)
        finish()
        return true
    }

    private fun setUpResponseLayout(response: String, success: Boolean) {
        binding.apply {
            responseType.setText(if (success) R.string.success else R.string.an_error_occurred)
            responseMessage.text = response

            imageView.setImageResource(if (success) R.drawable.ic_check else R.drawable.ic_error_outline)

            closeActivity.setOnClickListener { onBackPressed() }
            responseLayout.isVisible = true
        }
    }
//
// private void setupPasswordView() {
//     findViewById(R.id.passwordLayout).setVisibility(View.VISIBLE);
//     this.password = (EditText) findViewById(R.id.password);
//     this.password.addTextChangedListener(getTextListener());
//     this.passwordError = (TextView) findViewById(R.id.passwordError);
//     this.passwordRepeat = (EditText) findViewById(R.id.passwordRepeat);
//     this.passwordRepeat.addTextChangedListener(getTextListener());
//     this.passwordRepeatError = (TextView) findViewById(R.id.passwordRepeatError);
//     this.progressBar = (ExamProgressBar) findViewById(R.id.progressBar);
//
//     Button resetPassword = (Button) findViewById(R.id.resetPassword);
//     resetPassword.setOnClickListener(new ExamClickListener(this) {
//         @Override
//         public void onClicked(View view, Runnable runnable) {
//             progressBar.setVisibility(View.VISIBLE);
//             authManager.changePassword(tokenId, password.getText().toString(), response -> {
//                 progressBar.setVisibility(View.GONE);
//                 runnable.run();
//                 if (response.getType() != ResponseType.ERROR) {
//                     DialogBuilder builder = new DialogBuilder(ResetActivity.this, R.string.reset_password, response.getMessage());
//                     builder.setCancelable(false);
//                     builder.setPositiveButton(R.string.ok, () -> onBackPressed());
//                     builder.show();
//                 }
//             });
//         }
//     });
// }
//
// private TextWatcher getTextListener() {
//     return TextChangedListener.get((text) -> {
//         if (ValidationUtil.isPasswordValid(password.getText())) {
//             password.setBackgroundResource(R.drawable.form_edittext);
//             passwordError.setVisibility(View.GONE);
//         }
//         if (passwordRepeatError.getVisibility() == View.VISIBLE) {
//             if (password.getText().toString().equals(passwordRepeat.getText().toString())) {
//                 passwordRepeat.setBackgroundResource(R.drawable.form_edittext);
//                 passwordRepeatError.setVisibility(View.GONE);
//             }
//             return;
//         }
//         if (!TextUtils.isEmpty(passwordRepeat.getText())) {
//             passwordRepeat.setBackgroundResource(R.drawable.form_edittext);
//             passwordRepeatError.setVisibility(View.GONE);
//         }
//     });
// }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return super.onCreateOptionsMenu(menu)
    }

//
// @Override
// public boolean onInputCheck(boolean valid) {
//     if (!ValidationUtil.isPasswordValid(password.getText())) {
//         password.setBackgroundResource(R.drawable.form_edittext_error);
//         passwordError.setVisibility(View.VISIBLE);
//         valid = false;
//     }
//     if (TextUtils.isEmpty(passwordRepeat.getText())) {
//         passwordRepeat.setBackgroundResource(R.drawable.form_edittext_error);
//         valid = false;
//     }
//     if (!ValidationUtil.areEqual(password.getText(), passwordRepeat.getText())) {
//         passwordRepeat.setBackgroundResource(R.drawable.form_edittext_error);
//         passwordRepeatError.setVisibility(View.VISIBLE);
//         valid = false;
//     }
//     return valid;
// }
}