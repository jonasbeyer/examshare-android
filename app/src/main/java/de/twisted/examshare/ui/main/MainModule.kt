package de.twisted.examshare.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.ui.main.subjectlist.SubjectListFragment

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract fun contributeSubjectListFragment(): SubjectListFragment
}