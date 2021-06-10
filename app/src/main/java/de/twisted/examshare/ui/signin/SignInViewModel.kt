package de.twisted.examshare.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.user.UserRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import de.twisted.examshare.util.validation.ErrorMode
import de.twisted.examshare.util.validation.Validation
import de.twisted.examshare.util.validation.rules.ValidationEmptyRule
import de.twisted.examshare.util.validation.validateFormFields
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val identifier = MutableLiveData(Validation(listOf(ValidationEmptyRule())))
    val password = MutableLiveData(Validation(listOf(ValidationEmptyRule())))

    private val _errorMode = MutableLiveData<ErrorMode>(ErrorMode.None)
    val errorMode: LiveData<ErrorMode>
        get() = _errorMode

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>>
        get() = _navigateToMainActivity

    private val _navigateToSignUpAction = MutableLiveData<Event<Unit>>()
    val navigateToSignUpAction: LiveData<Event<Unit>>
        get() = _navigateToSignUpAction

    private val _navigateToResetPasswordAction = MutableLiveData<Event<Unit>>()
    val navigateToResetPasswordAction: LiveData<Event<Unit>>
        get() = _navigateToResetPasswordAction

    private val _signinErrorMessage = MutableLiveData<Event<Int>>()
    val signinErrorMessage: LiveData<Event<Int>>
        get() = _signinErrorMessage

    fun signIn() {
        if (!validateFormFields(_errorMode, identifier, password)) {
            Timber.e("Form valiation failed")
            return
        }

        val identifier = identifier.value!!.result!!.text
        val password = password.value!!.result!!.text

        compositeDisposable.add(userRepository.signIn(identifier, password)
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onSignInSuccess, ::onSignInError))
    }

    fun onSignUpClicked() {
        _navigateToSignUpAction.value = Event(Unit)
    }

    fun onPasswordResetClicked() {
        _navigateToResetPasswordAction.value = Event(Unit)
    }

    private fun onSignInSuccess() {
        _navigateToMainActivity.value = Event(Unit)
    }

    private fun onSignInError(throwable: Throwable) {
        val errorMessage: Int = when (throwable) {
            is IOException -> R.string.connection_error
//            is InvalidCredentialsException -> R.string.auth_invalid_credentials
//            is AccountDisabledException -> R.string.auth_account_disabled
//            is WrongRoleException -> R.string.auth_wrong_role
            else -> R.string.an_error_occurred
        }

        _signinErrorMessage.value = Event(content = errorMessage)
    }
}