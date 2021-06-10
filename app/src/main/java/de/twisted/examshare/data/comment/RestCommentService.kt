package de.twisted.examshare.data.comment

import de.twisted.examshare.data.models.Comment
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface RestCommentService {

    @POST("comments")
    @FormUrlEncoded
    fun addComment(
        @Field("taskId") taskId: String,
        @Field("threadId") threadId: String?,
        @Field("message") message: String
    ): Single<Comment>

    @PUT("comment/{commentId}")
    @FormUrlEncoded
    fun updateComment(
        @Path("commentId") commentId: String,
        @Field("message") message: String
    ): Single<Comment>

    @GET("comments")
    fun getComments(
        @Query("taskId") taskId: String,
        @Query("threadId") threadId: String?
    ): Single<List<Comment>>

    @PUT("comments/{commentId}/like_status")
    @FormUrlEncoded
    fun setLikeStatus(
        @Path("commentId") commentId: String,
        @Field("likeStatus") likeStatus: Int
    ): Completable

    @DELETE("comments/{commentId}")
    fun deleteComment(@Path("commentId") commentId: String): Completable
}