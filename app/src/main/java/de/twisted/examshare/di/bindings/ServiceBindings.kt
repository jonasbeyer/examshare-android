package de.twisted.examshare.di.bindings

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.service.firebase.MessagingService
import de.twisted.examshare.service.task.TaskUploadService

@Module
abstract class ServiceBindings {

    @ContributesAndroidInjector
    abstract fun contributeTaskUploadService(): TaskUploadService

    @ContributesAndroidInjector
    abstract fun contributeFirebaseMessagingService(): MessagingService
}