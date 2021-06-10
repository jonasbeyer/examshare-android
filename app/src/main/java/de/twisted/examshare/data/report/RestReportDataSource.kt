package de.twisted.examshare.data.report

import io.reactivex.Completable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RestReportDataSource {

    @POST("reports")
    @FormUrlEncoded
    fun addReport(
        @Field("itemId") itemId: String,
        @Field("itemType") itemType: String,
        @Field("reason") reason: String
    ): Completable
}