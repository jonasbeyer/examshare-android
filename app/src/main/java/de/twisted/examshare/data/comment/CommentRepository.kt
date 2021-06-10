package de.twisted.examshare.data.comment

import de.twisted.examshare.data.models.Comment
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentService: RestCommentService
) {

    fun addComment(taskId: String, threadId: String?, message: String): Completable =
        commentService.addComment(taskId, threadId, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElement()

    fun setLikeStatus(commentId: String, status: LikeStatus): Completable =
        commentService.setLikeStatus(commentId, status.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getComments(taskId: String, threadId: String?): Single<List<Comment>> =
        commentService.getComments(taskId, threadId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

enum class LikeStatus(val id: Int) {
    LIKED(1),
    DISLIKED(-1),
    NONE(0)
}