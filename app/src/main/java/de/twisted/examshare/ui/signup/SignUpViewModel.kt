package de.twisted.examshare.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.user.UserRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import de.twisted.examshare.util.validation.ErrorMode
import de.twisted.examshare.util.validation.Validation
import de.twisted.examshare.util.validation.rules.ValidationEmailRule
import de.twisted.examshare.util.validation.rules.ValidationPasswordRule
import de.twisted.examshare.util.validation.rules.ValidationUsernameRule
import de.twisted.examshare.util.validation.validateFormFields
import timber.log.Timber
import java.net.SocketException
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val username = MutableLiveData(Validation(listOf(ValidationUsernameRule())))
    val email = MutableLiveData(Validation(listOf(ValidationEmailRule())))
    val password = MutableLiveData(Validation(listOf(ValidationPasswordRule())))

    private val _errorMode = MutableLiveData<ErrorMode>(ErrorMode.None)
    val errorMode: LiveData<ErrorMode>
        get() = _errorMode

    private val _navigateToConfirmationDialog = MutableLiveData<Event<Unit>>()
    val navigateToConfirmationDialog: LiveData<Event<Unit>>
        get() = _navigateToConfirmationDialog

    private val _signUpError = MutableLiveData<Event<Int>>()
    val signUpError: LiveData<Event<Int>>
        get() = _signUpError

    fun signUp() {
        if (!validateFormFields(_errorMode, username, email, password)) {
            Timber.e("Form valiation failed")
            return
        }

        val username = username.value!!.result!!.text
        val email = email.value!!.result!!.text
        val password = password.value!!.result!!.text

        compositeDisposable.add(userRepository.signUp(username, email, password)
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onSignUpSuccess, ::onSignUpError))
    }

    private fun onSignUpSuccess() {
        _navigateToConfirmationDialog.value = Event(Unit)
    }

    private fun onSignUpError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        _signUpError.value = Event(errorMessage)
    }
}