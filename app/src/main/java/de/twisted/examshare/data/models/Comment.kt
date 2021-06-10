package de.twisted.examshare.data.models

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import de.twisted.examshare.R
import de.twisted.examshare.data.comment.LikeStatus
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Comment(
    val id: String,
    val message: String,
    val author: String,
    val authorId: String,
    var likeCount: Int,
    var dislikeCount: Int,
    val commentCount: Int,
    var liked: Boolean,
    var disliked: Boolean,
    val createdAt: Date,
    val updatedAt: Date
) : Parcelable {

//    fun getCreated(context: Context?): String {
//        return TimeUtil.getTimeAgo(context, created)
//    }

//    fun isTaskAuthorsComment(task: Task): Boolean {
//        return task.getAuthorId() === authorId
//    }

    fun getUpdateStatus(likeStatus: LikeStatus): LikeStatus {
        return if (likeStatus == LikeStatus.LIKED && liked || likeStatus == LikeStatus.DISLIKED && disliked) LikeStatus.NONE else likeStatus
    }

    fun setLiked() {
        if (liked) {
            liked = false
            likeCount--
            return
        }
        if (disliked) dislikeCount--
        likeCount++
        liked = true
        disliked = false
    }

    fun setDisliked() {
        if (disliked) {
            disliked = false
            dislikeCount--
            return
        }
        if (liked) likeCount--
        dislikeCount++
        liked = false
        disliked = true
    }

//    init {
//        author = ExamShareApplication.getInstance().getUserProfile().getUsername()
//        authorId = ExamShareApplication.getInstance().getUserProfile().getUserId()
//        this.message = message
//        created = System.currentTimeMillis()
//    }
}