package de.twisted.examshare.ui.taskcommon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.util.result.Event
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

interface TaskActionsViewModelDelegate : TaskActions {
    val navigateToTaskDetails: LiveData<Event<Task>>
    val snackbarMessage: LiveData<Event<Int>>
}

class DefaultTaskActionsViewModelDelegate @Inject constructor(
    private val taskRepository: TaskRepository
) : TaskActionsViewModelDelegate {

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    override val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarMessage

    private val _navigateToTaskDetails = MutableLiveData<Event<Task>>()
    override val navigateToTaskDetails: LiveData<Event<Task>>
        get() = _navigateToTaskDetails

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
}