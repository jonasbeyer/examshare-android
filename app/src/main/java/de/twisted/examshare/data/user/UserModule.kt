package de.twisted.examshare.data.user

import dagger.Module
import dagger.Provides
import de.twisted.examshare.data.user.datasource.RestUserService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UserModule {

    @Singleton
    @Provides
    fun provideRestUserService(retrofit: Retrofit): RestUserService = retrofit.create(RestUserService::class.java)

    @Provides
    fun bindUserSessionProvider(userStore: UserStore): UserSessionProvider = userStore
}