package de.twisted.examshare.ui.shared.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * The base class for all Android ViewModels that holds the disposables
 * and a live data object for the current loading state.
 */
abstract class BaseViewModel: ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    /**
     * Dispose all subscriptions
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}