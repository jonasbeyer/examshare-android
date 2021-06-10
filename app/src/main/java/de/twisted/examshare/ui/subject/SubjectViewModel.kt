package de.twisted.examshare.ui.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.ui.taskcommon.TaskActions
import de.twisted.examshare.util.extensions.setValueIfNew
import de.twisted.examshare.util.result.Event
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.net.SocketException
import javax.inject.Inject

class SubjectViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseViewModel(), TaskActions {

    private val _subjectId = MutableLiveData<String>()
    val subjectId: LiveData<String>
        get() = _subjectId

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

    private val _navigateToTaskDetailsActivity = MutableLiveData<Event<Task>>()
    val navigateToTaskDetailsActivity: LiveData<Event<Task>>
        get() = _navigateToTaskDetailsActivity

    private val _navigateToTaskDetailsDialog = MutableLiveData<Event<Task>>()
    val navigateToTaskDetailsDialog: LiveData<Event<Task>>
        get() = _navigateToTaskDetailsDialog

    private val _navigateToAddEditTaskAction = MutableLiveData<Event<Pair<String, Task?>>>()
    val navigateToAddEditTaskAction: LiveData<Event<Pair<String, Task?>>>
        get() = _navigateToAddEditTaskAction

    private val _navigateToReportTaskDialog = MutableLiveData<Event<String>>()
    val navigateToReportTaskDialog: LiveData<Event<String>>
        get() = _navigateToReportTaskDialog

    private val _navigateToDeleteTaskDialog = MutableLiveData<Event<String>>()
    val navigateToDeleteTaskDialog: LiveData<Event<String>>
        get() = _navigateToDeleteTaskDialog

    private val _navigateToTaskAuthorProfile = MutableLiveData<Event<String>>()
    val navigateToTaskAuthorProfile: LiveData<Event<String>>
        get() = _navigateToTaskAuthorProfile

    init {
        // Update observable source if the subject changes
        _taskList.addSource(_subjectId) { subjectId ->
            Timber.d("Refreshing data for subject $subjectId")
            listenForTaskChanges(subjectId)
        }
    }

    fun setSubjectId(subjectId: String) {
        _subjectId.setValueIfNew(subjectId)
    }

    fun onAddTaskClicked() {
        _subjectId.value?.let { subjectId ->
            _navigateToAddEditTaskAction.value = Event(Pair(subjectId, null))
        }
    }

    fun onSwipeRefresh() {
        _subjectId.value?.let { subject ->
            refreshTasks(subject)
        }
    }

    override fun openTaskDetailsActivity(task: Task) {
        _navigateToTaskDetailsActivity.value = Event(task)
    }

    override fun openTaskDetailsDialog(task: Task) {
        _navigateToTaskDetailsDialog.value = Event(task)
    }

    override fun openTaskEditAction(task: Task) {
        _navigateToAddEditTaskAction.value = Event(Pair(task.subjectId, task))
    }

    override fun openReportDialog(task: Task) {
        _navigateToReportTaskDialog.value = Event(task.id)
    }

    override fun openConfirmTaskDeleteDialog(task: Task) {
        _navigateToDeleteTaskDialog.value = Event(task.id)
    }

    override fun openAuthorProfile(authorId: String) {
        _navigateToTaskAuthorProfile.value = Event(authorId)
    }

    override fun onLikeClicked(task: Task) {
        val newIsFavoriteState = !task.isFavorite
        val stringResId = if (newIsFavoriteState) {
            R.string.favorite_add
        } else {
            R.string.favorite_remove
        }

        compositeDisposable.add(taskRepository.favoriteTask(
            task.copy(isFavorite = newIsFavoriteState)
        ).subscribeBy(
            onComplete = { emitSnackbarMessage(stringResId) },
            onError = { emitSnackbarMessage(R.string.favorite_error) }
        ))
    }

    private fun listenForTaskChanges(subjectId: String) {
        compositeDisposable.add(taskRepository.getObservableTasks(subjectId)
            .doOnSubscribe { setLoading(true) }
            .doOnNext { setLoading(false) }
            .subscribe(_taskList::setValue) { handleTaskListError(it, true) })
    }

    private fun refreshTasks(subjectId: String) {
        compositeDisposable.add(taskRepository.refreshTasks(subjectId)
            .doOnSubscribe { _swipeRefreshing.value = true }
            .doAfterTerminate { _swipeRefreshing.value = false }
            .subscribeBy(onError = { handleTaskListError(it, false) }))
    }

    private fun emitSnackbarMessage(resId: Int) {
        _snackbarMessage.value = Event(resId)
    }

    private fun handleTaskListError(throwable: Throwable, force: Boolean) {
        Timber.e("Error loading task data: $throwable")

        val errorMessageResId: Int = when (throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.an_error_occurred
        }

        if (force) {
            _errorMessage.value = errorMessageResId
        } else {
            _snackbarMessage.value = Event(errorMessageResId)
        }
    }
}