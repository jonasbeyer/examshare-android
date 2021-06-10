package de.twisted.examshare.data.auth

import de.twisted.examshare.BuildConfig
import de.twisted.examshare.data.models.SignInResult
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface RestAuthService {

    @POST("auth/signup")
    @FormUrlEncoded
    fun signUp(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("email") email: String
    ): Completable

    @POST("auth/signin")
    @FormUrlEncoded
    fun signIn(
            @Field("identifier") identifier: String,
            @Field("password") password: String
    ): Single<SignInResult>

    @POST("auth/signout")
    fun signOut(): Completable

    @POST("auth/reset_password")
    fun requestPasswordReset(@Field("email") email: String): Completable

    @POST("auth/request_verification")
    fun requestVerificationEmail(
            @Field("identifier") identifier: String,
            @Field("password") password: String,
            @Field("email") updatedEmail: String?
    ): Completable

    @GET("${BuildConfig.API_ENDPOINT}/verify/email?app=true")
    fun verifyTokenId(
            @Path("tokenId") tokenId: String,
            @Query("userId") userId: String
    ): Single<String>

    @PUT("auth/password")
    fun updatePassword(
            @Field("tokenId") tokenId: String,
            @Field("password") password: String
    ): Completable
}