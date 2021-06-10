package de.twisted.examshare.data.auth

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideRestAuthService(retrofit: Retrofit): RestAuthService = retrofit.create(RestAuthService::class.java)
}