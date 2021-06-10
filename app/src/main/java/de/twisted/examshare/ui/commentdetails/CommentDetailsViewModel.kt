package de.twisted.examshare.ui.commentdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.data.comment.CommentRepository
import de.twisted.examshare.data.comment.LikeStatus
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.ui.commentcommon.CommentActions
import de.twisted.examshare.util.result.Event
import javax.inject.Inject

class CommentDetailsViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseViewModel(), CommentActions {

    private val _parentComment = MutableLiveData<Comment>()
    val parentComment: LiveData<Comment>
        get() = _parentComment

    private val _commentList = MediatorLiveData<List<Comment>>().apply {
        addSource(_parentComment) { parentComment ->
            listenToCommentChanges(parentComment.id)
        }
    }
    val commentList: LiveData<List<Comment>> = _commentList

    private val _swipeRefreshing = MutableLiveData<Boolean>()
    val swipeRefreshing: LiveData<Boolean>
        get() = _swipeRefreshing

    private val _navigateToCommentAnswers = MutableLiveData<Event<Pair<Comment, Boolean>>>()
    val navigateToCommentAnswers: LiveData<Event<Pair<Comment, Boolean>>>
        get() = _navigateToCommentAnswers

    fun setParentComment(comment: Comment) {
        _parentComment.value = comment
    }

    fun onSwipeRefresh() {

    }

    private fun listenToCommentChanges(threadId: String) {

    }

    override fun openCommentAnswers(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun setLikeStatus(comment: Comment, likeStatus: LikeStatus) {
        TODO("Not yet implemented")
    }

    override fun openCommentAnswerEditor(comment: Comment, isParent: Boolean) {
        TODO("Not yet implemented")
    }

    override fun openCommentEditSheet(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun openCommentConfirmDeleteDialog(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun openAuthorProfile(userId: String) {
        TODO("Not yet implemented")
    }

    override fun openReportDialog(comment: Comment) {
        TODO("Not yet implemented")
    }
}