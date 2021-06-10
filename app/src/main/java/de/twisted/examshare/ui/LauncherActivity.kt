package de.twisted.examshare.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.LauncherViewModel.LaunchDestination
import de.twisted.examshare.ui.main.MainActivity
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.signin.SignInActivity
import de.twisted.examshare.util.result.EventUnhandledContentListener
import de.twisted.examshare.util.result.observeEvent
import javax.inject.Inject

class LauncherActivity : ExamActivity(), EventUnhandledContentListener<LaunchDestination> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<LauncherViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LauncherViewModel by viewModels { viewModelFactory }

        // Navigate the user to the determined activity
        viewModel.launchDestination.observeEvent(this, this)
    }

    override fun onEventUnhandledContent(value: LaunchDestination) {
        val intent = when (value) {
            LaunchDestination.MAIN_ACTIVITY -> Intent(this, MainActivity::class.java)
            LaunchDestination.SIGN_IN_ACTIVITY -> Intent(this, SignInActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}