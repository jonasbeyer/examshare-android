package de.twisted.examshare.ui.taskdetails

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.ui.taskdetails.comments.TaskCommentListFragment

@Module
abstract class TaskDetailsModule {

    @ContributesAndroidInjector
    abstract fun contributeTaskCommentsFragment(): TaskCommentListFragment
}