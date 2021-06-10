package de.twisted.examshare.util.http

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import de.twisted.examshare.ExamShareApplication
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject

@Excludes(OkHttpLibraryGlideModule::class)
@GlideModule
class OkHttpGlideModule : AppGlideModule() {

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        inject(context)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }

    private fun inject(context: Context) {
        (context.applicationContext as ExamShareApplication)
            .androidInjector()
            .inject(this)
    }
}