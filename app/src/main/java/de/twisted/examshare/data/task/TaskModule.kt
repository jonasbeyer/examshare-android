package de.twisted.examshare.data.task

import dagger.Module
import dagger.Provides
import de.twisted.examshare.data.db.AppDatabase
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class TaskModule {

    @Singleton
    @Provides
    fun provideRestTaskService(retrofit: Retrofit): RestTaskService = retrofit.create(RestTaskService::class.java)

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
}