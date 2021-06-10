package de.twisted.examshare.ui.signin.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.user.UserRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import de.twisted.examshare.util.validation.ErrorMode
import de.twisted.examshare.util.validation.Validation
import de.twisted.examshare.util.validation.rules.ValidationEmailRule
import de.twisted.examshare.util.validation.validateFormFields
import timber.log.Timber
import java.net.SocketException
import javax.inject.Inject

class RequestPasswordResetViewModel @Inject constructor(
        private val userRepository: UserRepository
) : BaseViewModel() {

    val emailAddress = MutableLiveData(Validation(listOf(ValidationEmailRule())))

    private val _errorMode = MutableLiveData<ErrorMode>(ErrorMode.None)
    val errorMode: LiveData<ErrorMode>
        get() = _errorMode

    private val _navigateToConfirmationDialog = MutableLiveData<Event<Unit>>()
    val navigateToConfirmationDialog: LiveData<Event<Unit>>
        get() = _navigateToConfirmationDialog

    private val _passwordResetError = MutableLiveData<Event<Int>>()
    val passwordResetError: LiveData<Event<Int>>
        get() = _passwordResetError

    fun requestPasswordReset() {
        if (!validateFormFields(_errorMode, emailAddress)) {
            Timber.e("Form valiation failed")
            return
        }

        val emailAddress = emailAddress.value!!.result!!.text
        compositeDisposable.add(userRepository.requestPasswordReset(emailAddress)
                .doOnSubscribe { setLoading(true) }
                .doAfterTerminate { setLoading(false) }
                .subscribe(::onPasswordResetSuccess, ::onPasswordResetError))
    }

    private fun onPasswordResetSuccess() {
        _navigateToConfirmationDialog.value = Event(Unit)
    }

    private fun onPasswordResetError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        _passwordResetError.value = Event(errorMessage)
    }
}