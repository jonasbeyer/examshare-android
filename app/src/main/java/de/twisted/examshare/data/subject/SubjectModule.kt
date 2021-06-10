package de.twisted.examshare.data.subject

import dagger.Module
import dagger.Provides
import de.twisted.examshare.data.db.AppDatabase
import de.twisted.examshare.data.task.TaskDao
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class SubjectModule {

    @Provides
    @Singleton
    fun provideRestSubjectService(retrofit: Retrofit): RestSubjectService =
        retrofit.create(RestSubjectService::class.java)

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()
}