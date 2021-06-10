package de.twisted.examshare.di.bindings

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.ui.report.ReportDialogFragment
import de.twisted.examshare.ui.taskcommon.TaskDeleteDialogFragment

@Module
abstract class FragmentBindings {

    @ContributesAndroidInjector
    abstract fun contributeReportDialogFragment(): ReportDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeTaskDeleteDialogFragment(): TaskDeleteDialogFragment
}