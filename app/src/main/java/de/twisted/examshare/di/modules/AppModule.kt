package de.twisted.examshare.di.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import de.twisted.examshare.ExamShareApplication
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: ExamShareApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}