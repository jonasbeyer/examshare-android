package de.twisted.examshare.di.bindings

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.ui.LauncherActivity
import de.twisted.examshare.ui.account.AccountActivity
import de.twisted.examshare.ui.addedittask.AddEditTaskActivity
import de.twisted.examshare.ui.commentdetails.CommentDetailsActivity
import de.twisted.examshare.ui.main.MainActivity
import de.twisted.examshare.ui.main.MainModule
import de.twisted.examshare.ui.settings.SettingsActivity
import de.twisted.examshare.ui.signin.SignInActivity
import de.twisted.examshare.ui.signin.find.RequestPasswordResetActivity
import de.twisted.examshare.ui.signup.SignUpActivity
import de.twisted.examshare.ui.subject.SubjectActivity
import de.twisted.examshare.ui.taskdetails.TaskDetailsActivity
import de.twisted.examshare.ui.taskdetails.TaskDetailsModule
import de.twisted.examshare.ui.taskdetails.TaskFullImageActivity
import de.twisted.examshare.ui.tasklist.TaskListActivity

@Module
abstract class ActivityBindings {

    @ContributesAndroidInjector
    abstract fun contributeLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun contributeSignInActivity(): SignInActivity

    @ContributesAndroidInjector
    abstract fun contributeSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun contributeRequestPasswordResetActivity(): RequestPasswordResetActivity

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSubjectActivity(): SubjectActivity

    @ContributesAndroidInjector(modules = [TaskDetailsModule::class])
    abstract fun contributeTaskDetailsActivity(): TaskDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeTaskFullImageActivity(): TaskFullImageActivity

    @ContributesAndroidInjector
    abstract fun contributeTaskListActivity(): TaskListActivity

    @ContributesAndroidInjector
    abstract fun contributeAddEditTaskActivity(): AddEditTaskActivity

    @ContributesAndroidInjector
    abstract fun contributeCommentDetailsActivity(): CommentDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeAccountActivity(): AccountActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity
}