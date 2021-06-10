package de.twisted.examshare.data.user

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.models.User
import de.twisted.examshare.data.models.UserRole
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val context: Context
) : UserSessionProvider {

    companion object {
        private const val PREF_NAME = "user"
        private const val TOKEN = "token"

        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val ROLE_ID = "roleId"
        private const val ROLE_NAME = "roleName"
        private const val TASKS = "tasks"
        private const val CREATED_AT = "createdAt"
        private const val RATING_AVERAGE = "ratingAverage"
        private const val NOTIFICATIONS = "notifications"
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        observableUser.onNext(getUser())
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .apply { registerOnSharedPreferenceChangeListener(changeListener) }

    private val observableUser = BehaviorSubject.createDefault(getUser())

    fun insertUserSession(user: User, token: String) = sharedPreferences.edit {
        putString(TOKEN, token)
        putString(ID, user.id)
        putString(USERNAME, user.username)
        putString(EMAIL, user.email)
        putInt(ROLE_ID, user.role.id)
        putString(ROLE_NAME, user.role.name)
        putInt(TASKS, user.tasks)
        putLong(CREATED_AT, user.createdAt.time)
        putFloat(RATING_AVERAGE, user.ratingAverage)
        putStringSet(NOTIFICATIONS, user.notifications)
    }

    fun getUser(): User = User(
        id = sharedPreferences.getString(ID, "")!!,
        username = sharedPreferences.getString(USERNAME, "")!!,
        email = sharedPreferences.getString(EMAIL, "")!!,
        role = UserRole(
            id = sharedPreferences.getInt(ROLE_ID, 0),
            name = sharedPreferences.getString(ROLE_NAME, "")!!
        ),
        tasks = sharedPreferences.getInt(TASKS, 0),
        createdAt = Date(sharedPreferences.getLong(CREATED_AT, 0)),
        ratingAverage = sharedPreferences.getFloat(RATING_AVERAGE, 0F),
        notifications = sharedPreferences.getStringSet(NOTIFICATIONS, emptySet())!!,
        properties = emptyMap()
    )

    fun isSignedIn(): Boolean = getSessionToken() != null

    fun getObservableUser(): Observable<User> = observableUser

    fun updateNotificationPreference(subject: Subject, isEnabled: Boolean) {
        val currentPreferences = sharedPreferences.getStringSet(NOTIFICATIONS, emptySet())!!

        if (isEnabled) {
            currentPreferences.add(subject.name)
        } else {
            currentPreferences.remove(subject.name)
        }

        sharedPreferences.edit {
            putStringSet(NOTIFICATIONS, currentPreferences)
        }
    }

    fun deleteSession() {
        sharedPreferences.edit().clear().apply()
    }

    override fun getSessionToken(): String? = sharedPreferences.getString(TOKEN, null)
}