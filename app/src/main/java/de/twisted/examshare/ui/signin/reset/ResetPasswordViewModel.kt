package de.twisted.examshare.ui.signin.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.data.auth.RestAuthService
import de.twisted.examshare.ui.account.AccountViewModelDelegate
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.extensions.setValueIfNew
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(
        private val authService: RestAuthService,
        private val accountViewModelDelegate: AccountViewModelDelegate
) : BaseViewModel(), AccountViewModelDelegate by accountViewModelDelegate {

    private val _tokenValue = MutableLiveData<Pair<String, String>>()
    val tokenValue: LiveData<Pair<String, String>>
        get() = _tokenValue

    private val _tokenValidationResult = MediatorLiveData<Result<String>>()
    val tokenValiationResult: LiveData<Result<String>>
        get() = _tokenValidationResult

    fun setTokenValue(tokenId: String, userId: String) {
        _tokenValue.setValueIfNew(Pair(tokenId, userId))
    }

    init {
        _tokenValidationResult.addSource(tokenValue) { tokenValue ->
            verifyTokenId(tokenValue.first, tokenValue.second)
        }
    }

    // TODO: Test
    private fun verifyTokenId(tokenId: String, userId: String) {
        compositeDisposable.add(authService.verifyTokenId(tokenId, userId)
                .doOnSubscribe { setLoading(true) }
                .doAfterTerminate { setLoading(false) }
                .subscribe())
    }

    fun updatePassword(tokenId: String, password: String) {
        compositeDisposable.add(authService.updatePassword(tokenId, password)
                .doOnSubscribe { setLoading(true) }
                .doAfterTerminate { setLoading(false) }
                .subscribe())
    }
}