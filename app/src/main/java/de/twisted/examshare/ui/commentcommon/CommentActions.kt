package de.twisted.examshare.ui.commentcommon

import de.twisted.examshare.data.comment.LikeStatus
import de.twisted.examshare.data.models.Comment

interface CommentActions {
    fun openCommentAnswers(comment: Comment)
    fun openCommentAnswerEditor(comment: Comment, isParent: Boolean)
    fun setLikeStatus(comment: Comment, likeStatus: LikeStatus)

    fun openCommentEditSheet(comment: Comment)
    fun openCommentConfirmDeleteDialog(comment: Comment)
    fun openAuthorProfile(userId: String)
    fun openReportDialog(comment: Comment)
}