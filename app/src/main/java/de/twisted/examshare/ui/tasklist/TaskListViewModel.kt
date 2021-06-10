package de.twisted.examshare.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.ui.account.AccountViewModelDelegate
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.ui.taskcommon.TaskActions
import de.twisted.examshare.util.extensions.setValueIfNew
import de.twisted.examshare.util.result.Event
import timber.log.Timber
import java.net.SocketException
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val accountViewModelDelegate: AccountViewModelDelegate
) : BaseViewModel(),
    TaskActions,
    AccountViewModelDelegate by accountViewModelDelegate {

    private val _userId = MutableLiveData<String>()

    private val _swipeRefreshing = MutableLiveData<Boolean>()
    val swipeRefreshing: LiveData<Boolean>
        get() = _swipeRefreshing

    private val _taskList = MediatorLiveData<List<Task>>()
    val taskList: LiveData<List<Task>>
        get() = _taskList

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarMessage

    private val _navigateToTaskDetails = MutableLiveData<Event<Task>>()
    val navigateToTaskDetails: LiveData<Event<Task>>
        get() = _navigateToTaskDetails

    init {
        _taskList.addSource(_userId) { userId ->
            Timber.d("Refreshing data for userId $userId")
            listenToTaskChanges(userId)
        }
    }

    fun setUserId(userId: String) {
        _userId.setValueIfNew(userId)
    }

    fun onSwipeRefresh() {
        _userId.value?.let { userId ->
            refreshTasks(userId)
        }
    }

    override fun openTaskDetailsActivity(task: Task) {

    }

    override fun openTaskDetailsDialog(task: Task) {

    }

    override fun openTaskEditAction(task: Task) {

    }

    override fun openReportDialog(task: Task) {

    }

    override fun openConfirmTaskDeleteDialog(task: Task) {

    }

    override fun openAuthorProfile(authorId: String) {

    }

    override fun onLikeClicked(task: Task) {

    }

    private fun listenToTaskChanges(userId: String) {
        compositeDisposable.add(taskRepository.getObservableTasks(userId)
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onRetrieveTaskListSuccess) { throwable ->
                onRetrieveTaskListError(throwable, true)
            })
    }

    private fun refreshTasks(userId: String) {
        compositeDisposable.add(taskRepository.refreshTasks(userId)
            .doOnSubscribe { _swipeRefreshing.value = true }
            .doAfterTerminate { _swipeRefreshing.value = false }
            .subscribe({}, { onRetrieveTaskListError(it, false) }))
    }

    private fun onRetrieveTaskListSuccess(taskList: List<Task>) {
        _taskList.value = taskList
    }

    private fun onRetrieveTaskListError(throwable: Throwable, force: Boolean) {
        val errorMessage: Int = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        if (force) {
            _errorMessage.value = errorMessage
        } else {
            _snackbarMessage.value = Event(errorMessage)
        }
    }
}