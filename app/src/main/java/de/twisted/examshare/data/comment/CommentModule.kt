package de.twisted.examshare.data.comment

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CommentModule {

    @Singleton
    @Provides
    fun provideRestCommentService(retrofit: Retrofit): RestCommentService =
        retrofit.create(RestCommentService::class.java)
}