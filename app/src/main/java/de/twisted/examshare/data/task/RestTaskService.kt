package de.twisted.examshare.data.task

import de.twisted.examshare.data.models.Task
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface RestTaskService {

    @POST("tasks")
    fun createTask(@Body requestBody: RequestBody): Single<Task>

    @PUT("tasks/{taskId}")
    fun updateTask(@Path("taskId") taskId: String, @Body task: Task): Single<Task>

    @GET("tasks")
    fun getTasks(@Query("subjectId") subjectId: String): Single<List<Task>>

    @POST("tasks/{taskId}/rating")
    @FormUrlEncoded
    fun rateTask(
        @Path("taskId") taskId: String,
        @Field("rating") rating: Int
    ): Completable

    @PUT("tasks/{taskId}/favorite")
    @FormUrlEncoded
    fun favoriteTask(
        @Path("taskId") taskId: String,
        @Field("isFavorite") isFavorite: Boolean
    ): Completable

    @DELETE("tasks/{taskId}")
    fun deleteTask(@Path("taskId") taskId: String): Completable
}