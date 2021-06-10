package de.twisted.examshare.ui.taskcommon

import de.twisted.examshare.data.models.Task

interface TaskActions {
    fun onLikeClicked(task: Task)

    fun openTaskDetailsActivity(task: Task)

    fun openTaskDetailsDialog(task: Task)

    fun openTaskEditAction(task: Task)

    fun openConfirmTaskDeleteDialog(task: Task)

    fun openAuthorProfile(authorId: String)

    fun openReportDialog(task: Task)
}