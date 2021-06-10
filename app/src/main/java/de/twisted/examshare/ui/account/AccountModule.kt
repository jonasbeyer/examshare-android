package de.twisted.examshare.ui.account

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.twisted.examshare.ui.account.settings.ChangeEmailDialogFragment
import de.twisted.examshare.ui.account.settings.ChangePasswordDialogFragment
import de.twisted.examshare.ui.account.signout.SignOutDialogFragment

@Module
abstract class AccountModule {

    @ContributesAndroidInjector
    abstract fun contributeSignOutDialogFragment(): SignOutDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeChangeEmailDialogFragment(): ChangeEmailDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeChangePasswordDialogFragment(): ChangePasswordDialogFragment

    @Binds
    abstract fun bindAccountViewModelDelegate(accountViewModelDelegate: DefaultAccountViewModelDelegate): AccountViewModelDelegate
}