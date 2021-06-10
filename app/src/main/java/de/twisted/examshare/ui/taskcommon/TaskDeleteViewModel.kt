package de.twisted.examshare.ui.taskcommon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.result.Event
import io.reactivex.disposables.Disposable
import java.net.SocketException
import javax.inject.Inject

class TaskDeleteViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    private var _itemId: String? = null
    private var deleteItemDisposable: Disposable? = null

    private val _navigateToDismissAction = MutableLiveData<Event<Unit>>()
    val navigateToDismissAction: LiveData<Event<Unit>>
        get() = _navigateToDismissAction

    private val _itemDeletionError = MutableLiveData<Event<Int>>()
    val itemDeletionError: LiveData<Event<Int>>
        get() = _itemDeletionError

    fun deleteItem(itemId: String) {
        deleteItemDisposable?.dispose()
        deleteItemDisposable = taskRepository.deleteTask(itemId)
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onItemDeleteSuccess, ::onItemDeleteError)
    }

    private fun onItemDeleteSuccess() {
        _navigateToDismissAction.value = Event(Unit)
    }

    private fun onItemDeleteError(throwable: Throwable) {
        val errorResId = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        _itemDeletionError.value = Event(errorResId)
    }

    override fun onCleared() {
        super.onCleared()
        deleteItemDisposable?.dispose()
    }
}