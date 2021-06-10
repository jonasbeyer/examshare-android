package de.twisted.examshare.di;

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import de.twisted.examshare.ExamShareApplication
import de.twisted.examshare.data.DataModule
import de.twisted.examshare.di.bindings.ActivityBindings
import de.twisted.examshare.di.bindings.FragmentBindings
import de.twisted.examshare.di.bindings.GlideBindings
import de.twisted.examshare.di.bindings.ServiceBindings
import de.twisted.examshare.di.modules.AppModule
import de.twisted.examshare.ui.account.AccountModule
import javax.inject.Singleton

/**
 * Main component of the app created in the Application class.
 *
 * The list of modules contains the binding modules, dependency modules and
 * the [AndroidInjectionModule] that helps to integrate with the Android framework.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    DataModule::class,
    AccountModule::class,
    ActivityBindings::class,
    FragmentBindings::class,
    ServiceBindings::class,
    GlideBindings::class
])
interface AppComponent : AndroidInjector<ExamShareApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<ExamShareApplication>
}
