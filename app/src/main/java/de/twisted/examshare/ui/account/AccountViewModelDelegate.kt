package de.twisted.examshare.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.data.models.User
import de.twisted.examshare.data.user.UserRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

interface AccountViewModelDelegate {
    val delegateCompositeDisposable: CompositeDisposable

    val currentUserInfo: LiveData<User>

    fun getUserId(): String?

    fun isSignedIn(): Boolean

    fun isCurrent(userId: String): Boolean
}

class DefaultAccountViewModelDelegate @Inject constructor(
        private val userRepository: UserRepository
) : AccountViewModelDelegate {

    override val delegateCompositeDisposable = CompositeDisposable()

    private val _currentUserInfo = MutableLiveData<User>()
    override val currentUserInfo: LiveData<User>
        get() = _currentUserInfo

    init {
        delegateCompositeDisposable.add(userRepository
                .observeCurrentUser()
                .subscribe(_currentUserInfo::setValue))
    }

    override fun getUserId(): String? {
        return _currentUserInfo.value?.id
    }

    override fun isSignedIn(): Boolean {
        return userRepository.isUserSignedIn()
    }

    override fun isCurrent(userId: String): Boolean {
        return getUserId() == userId;
    }
}