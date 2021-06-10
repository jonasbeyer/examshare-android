package de.twisted.examshare.data.user

import de.twisted.examshare.data.auth.RestAuthService
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.models.User
import de.twisted.examshare.data.user.datasource.RestUserService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A single point of entry repository that handles the
 * authentication and user account actions
 */
@Singleton
class UserRepository @Inject constructor(
    private val authService: RestAuthService,
    private val userService: RestUserService,
    private val userStore: UserStore
) {

    /**
     * Send a signin request to the backend's auth service
     *
     * If the credentials are correct, the server returns an
     * authentication token that is stored in the local user store
     * together with the user's account information.
     *
     * @param identifier the user's name or email address
     * @param password   the user's password
     * @return           a RxJava [Completable] that completes after the signin process has finished successfully
     */
    fun signIn(identifier: String, password: String): Completable {
        return authService
            .signIn(identifier, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { userStore.insertUserSession(it.user, it.token) }
            .ignoreElement()
    }

    /**
     * Send a registration request to the backend's auth service
     *
     * @param username the user's public identifier
     * @param email    the user's email address that the verification email is sent to
     * @param password the user's password that is needed for sign in
     * @return         a RxJava [Completable] that completes after the registration process has finished successfully
     */
    fun signUp(username: String, email: String, password: String): Completable {
        return authService
            .signUp(username, password, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Send a sign out request to the backend's auth service to
     * invalidate the current session token.
     *
     * @return a RxJava [Completable] that completes after the request has finished successfully
     */
    fun signOut(): Completable {
        return authService.signOut()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen { userStore.deleteSession() }
    }

    /**
     * @return the user's current authentication status
     */
    fun isUserSignedIn(): Boolean = userStore.isSignedIn()

    /**
     * Send a request to the backend's auth service to trigger the mail service
     * that should send a password reset email.
     *
     * @param email the user's email address
     * @return      a RxJava [Completable] that completes after the email has been sent
     *
     */
    fun requestPasswordReset(email: String): Completable {
        return authService.requestPasswordReset(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Update the current user's email address
     *
     * @param emailAddress the user's new email address
     * @return             a RxJava [Completable] that completes after the email has been updated
     */
    fun updateEmail(emailAddress: String): Completable = userService.updateEmailAddress(emailAddress)

    /**
     * Update the current user's password.
     * The current session token shouldn't be destroyed.
     *
     * @param oldPassword the user's current password
     * @param newPassword the user's new password
     * @return            a RxJava [Completable] that completes after the password has been updated
     */
    fun updatePassword(oldPassword: String, newPassword: String): Completable {
        return userService
            .updatePassword(oldPassword, newPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Disable the user's current account before it is deleted permanently.
     *
     * @param password the user's password
     * @return         a RxJava [Completable] that completes after the account has been disabled
     */
    fun disableAccount(password: String): Completable {
        return this.userService.disableAccount(password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Observe the current user's account information to
     * receive immediate updates
     */
    fun observeCurrentUser(): Observable<User> = userStore.getObservableUser()
}