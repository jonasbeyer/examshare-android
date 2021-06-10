package de.twisted.examshare.di.bindings

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.util.http.OkHttpGlideModule

@Module
abstract class GlideBindings {

    @ContributesAndroidInjector
    abstract fun contributeHttpGlideModule(): OkHttpGlideModule
}