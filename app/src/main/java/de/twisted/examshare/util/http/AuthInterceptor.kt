package de.twisted.examshare.util.http

import android.os.Build
import de.twisted.examshare.BuildConfig
import de.twisted.examshare.ExamShareApplication
import de.twisted.examshare.data.user.UserSessionProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
        private val userSessionProvider: UserSessionProvider
) : Interceptor {

    private val userAgent = String.format(USER_AGENT, Build.MODEL, Build.VERSION.RELEASE, BuildConfig.VERSION_NAME)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
                .header("user-agent", userAgent)
                .header("app-key", "EDb)X/Q(663H&yA%br;7")
                .header("authorization", "Bearer ${userSessionProvider.getSessionToken() ?: ""}")
                .build()

        return chain.proceed(request)
    }

    companion object {
        private const val USER_AGENT = "%s (Android %s); App-Version: %s"
    }
}