package de.twisted.examshare.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.report.RestReportDataSource
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketException
import javax.inject.Inject

class ReportViewModel @Inject constructor(
    private val reportDataSource: RestReportDataSource
) : BaseViewModel() {

    private var _itemId: String? = null
    private var _itemType: String? = null

    private var submitReportDisposable: Disposable? = null

    private val _navigateToDismissAction = MutableLiveData<Event<Unit>>()
    val navigateToDismissAction: LiveData<Event<Unit>>
        get() = _navigateToDismissAction

    private val _submitReportErrorMessage = MutableLiveData<Event<Int>>()
    val submitReportErrorMessage: LiveData<Event<Int>>
        get() = _submitReportErrorMessage

    fun setItem(item: Pair<String, String>) {
        _itemId = item.first
        _itemType = item.second
    }

    fun submitReport(reason: String) {
        val itemId = _itemId ?: return
        val itemType = _itemType ?: return

        submitReportDisposable?.dispose()
        submitReportDisposable = reportDataSource.addReport(itemId, itemType, reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onSubmitReportSuccess, ::onSubmitReportError)
    }

    private fun onSubmitReportSuccess() {
        _navigateToDismissAction.value = Event(Unit)
    }

    private fun onSubmitReportError(throwable: Throwable) {
        val errorResId = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        _submitReportErrorMessage.value = Event(errorResId)
    }

    override fun onCleared() {
        super.onCleared()
        submitReportDisposable?.dispose()
    }
}