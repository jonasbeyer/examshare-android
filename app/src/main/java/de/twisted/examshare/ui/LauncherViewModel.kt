package de.twisted.examshare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.data.user.UserRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import javax.inject.Inject

/**
 * ViewModel for the LauncherActivity to navigate the user to the determined activity.
 */
class LauncherViewModel @Inject constructor(
    userRepository: UserRepository
) : BaseViewModel() {

    private val _launchDestination = MutableLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>>
        get() = _launchDestination

    init {
        val signedIn = userRepository.isUserSignedIn()
        val destination = LaunchDestination.values().find { it.signedIn == signedIn }!!

        // Navigate the user to the determined destination
        _launchDestination.value = Event(destination)
    }

    enum class LaunchDestination(val signedIn: Boolean) {
        MAIN_ACTIVITY(true),
        SIGN_IN_ACTIVITY(false)
    }
}

