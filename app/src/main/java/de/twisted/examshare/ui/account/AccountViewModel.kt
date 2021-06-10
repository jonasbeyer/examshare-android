package de.twisted.examshare.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.data.user.UserRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import de.twisted.examshare.util.result.Result
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accountViewModelDelegate: AccountViewModelDelegate
) : BaseViewModel(), AccountViewModelDelegate by accountViewModelDelegate {

    private val _accountUpdateLoading = MutableLiveData<Boolean>()
    val accountUpdateLoading: LiveData<Boolean>
        get() = _accountUpdateLoading

    private val _accountUpdateResult = MutableLiveData<Event<Result<Unit>>>()
    val accountUpdateResult: LiveData<Event<Result<Unit>>>
        get() = _accountUpdateResult

    fun updatePassword(oldPassword: String, newPassword: String) {
        compositeDisposable.add(userRepository.updatePassword(oldPassword, newPassword)
            .doOnSubscribe { _accountUpdateLoading.value = true }
            .doAfterTerminate { _accountUpdateLoading.value = false }
            .subscribe())
    }

    fun updateEmail(emailAddress: String) {
        compositeDisposable.add(userRepository.updateEmail(emailAddress)
            .doOnSubscribe { _accountUpdateLoading.value = true }
            .doAfterTerminate { _accountUpdateLoading.value = false }
            .subscribe())
    }

    fun signOut() {
        compositeDisposable.add(userRepository.signOut()
            .doOnSubscribe { _accountUpdateLoading.value = true }
            .doAfterTerminate { _accountUpdateLoading.value = false }
            .subscribe())
    }
}