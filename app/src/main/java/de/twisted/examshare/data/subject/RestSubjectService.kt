package de.twisted.examshare.data.subject

import de.twisted.examshare.data.models.Subject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface RestSubjectService {

    @GET("subjects")
    fun getSubjects(): Single<List<Subject>>

    @PUT("subjects/{subjectId}/notifications_enabled")
    @FormUrlEncoded
    fun updateSubjectNotificationsEnabled(
        @Path("subjectId") subjectId: String,
        @Field("isEnabled") isEnabled: Boolean
    ): Completable
}