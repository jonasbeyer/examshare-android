package de.twisted.examshare.ui.taskdetails.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.comment.CommentRepository
import de.twisted.examshare.data.comment.LikeStatus
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.ui.account.AccountViewModelDelegate
import de.twisted.examshare.ui.commentcommon.CommentActions
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.extensions.setValueIfNew
import de.twisted.examshare.util.result.Event
import timber.log.Timber
import javax.inject.Inject

class TaskCommentListViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val accountViewModelDelegate: AccountViewModelDelegate
) : BaseViewModel(), CommentActions, AccountViewModelDelegate by accountViewModelDelegate {

    private val _taskId = MutableLiveData<String>()

    private val _commentList = MediatorLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private val _swipeRefreshing = MutableLiveData<Boolean>()
    val swipeRefreshing: LiveData<Boolean>
        get() = _swipeRefreshing

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarMessage

    private val _navigateToCommentAnswers = MutableLiveData<Event<Pair<Comment, Boolean>>>()
    val navigateToCommentAnswers: LiveData<Event<Pair<Comment, Boolean>>>
        get() = _navigateToCommentAnswers

    private val _navigateToAddEditCommentAction = MutableLiveData<Event<Comment?>>()
    val navigateToAddEditCommentAction: LiveData<Event<Comment?>>
        get() = _navigateToAddEditCommentAction

    private val _navigateToCommentAuthorProfile = MutableLiveData<Event<String>>()
    val navigateToCommentAuthorProfile: LiveData<Event<String>>
        get() = _navigateToCommentAuthorProfile

    init {
        _commentList.addSource(_taskId) { taskId ->
            Timber.d("Refreshing comment data for taskId $taskId")
            refreshCommentList(taskId, true)
        }
    }

    fun setTaskId(taskId: String) {
        _taskId.setValueIfNew(taskId);
    }

    private fun refreshCommentList(taskId: String, force: Boolean) {
        compositeDisposable.add(commentRepository.getComments(taskId, null)
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(_commentList::setValue) { onCommentListLoadingError(it, force) })
    }

    override fun setLikeStatus(comment: Comment, likeStatus: LikeStatus) {
        compositeDisposable
            .add(commentRepository.setLikeStatus(comment.id, likeStatus)
                .subscribe())
    }

    override fun openCommentAnswers(comment: Comment) {
        _navigateToCommentAnswers.value = Event(Pair(comment, false))
    }

    override fun openCommentAnswerEditor(comment: Comment, isParent: Boolean) {
        if (isParent) {
            _navigateToAddEditCommentAction.value = Event(null)
        } else {
            _navigateToCommentAnswers.value = Event(Pair(comment, true))
        }
    }

    override fun openCommentEditSheet(comment: Comment) {
        _navigateToAddEditCommentAction.value = Event(comment)
    }

    override fun openCommentConfirmDeleteDialog(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun openAuthorProfile(userId: String) {
        _navigateToCommentAuthorProfile.value = Event(userId)
    }

    override fun openReportDialog(comment: Comment) {
        TODO("Not yet implemented")
    }

    fun submitComment(message: String) {
        _taskId.value?.let {
            commentRepository.addComment(it, null, message).subscribe()
        }
    }

    fun onSwipeRefresh() {
        _taskId.value?.let { taskId ->
            refreshCommentList(taskId, false)
        }
    }

    private fun onCommentListLoadingError(throwable: Throwable, force: Boolean) {
        if (force) {

        } else {
            _snackbarMessage.value = Event(R.string.an_error_occurred)
        }
    }

    override fun onCleared() {
        super.onCleared()
        delegateCompositeDisposable.dispose()
    }
}