package de.twisted.examshare.ui.taskdetails

import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.ui.account.AccountViewModelDelegate
import de.twisted.examshare.ui.shared.base.BaseViewModel
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
        private val taskRepository: TaskRepository,
        private val accountViewModelDelegate: AccountViewModelDelegate
): BaseViewModel(), AccountViewModelDelegate by accountViewModelDelegate {

}