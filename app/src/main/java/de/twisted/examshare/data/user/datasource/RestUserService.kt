package de.twisted.examshare.data.user.datasource

import io.reactivex.Completable
import retrofit2.http.*

interface RestUserService {

    @PUT("users/me/profileImage")
    fun updateProfileImage(): Completable

    @PUT("users/me/properties")
    fun updateProperties(): Completable

    @PUT("users/me/email")
    @FormUrlEncoded
    fun updateEmailAddress(@Field("email") emailAddress: String): Completable

    @PUT("users/me/password")
    @FormUrlEncoded
    fun updatePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") password: String
    ): Completable

    @POST("users/me/disable")
    @FormUrlEncoded
    fun disableAccount(@Field("password") password: String): Completable
}