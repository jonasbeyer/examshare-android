package de.twisted.examshare.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.twisted.examshare.BuildConfig
import de.twisted.examshare.data.auth.AuthModule
import de.twisted.examshare.data.comment.CommentModule
import de.twisted.examshare.data.db.AppDatabase
import de.twisted.examshare.data.report.ReportModule
import de.twisted.examshare.data.subject.SubjectModule
import de.twisted.examshare.data.task.TaskModule
import de.twisted.examshare.data.user.UserModule
import de.twisted.examshare.util.http.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [
    AuthModule::class,
    CommentModule::class,
    UserModule::class,
    SubjectModule::class,
    TaskModule::class,
    ReportModule::class
])
class DataModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
    }
}