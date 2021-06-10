package de.twisted.examshare.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.messaging.FirebaseMessaging
import de.twisted.examshare.R
import de.twisted.examshare.service.task.TaskUploadService
import de.twisted.examshare.ui.LauncherActivity
import de.twisted.examshare.ui.account.AccountActivity
import de.twisted.examshare.ui.settings.SettingsActivity
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.signin.SignInActivity
import de.twisted.examshare.ui.signin.reset.ResetPasswordActivity
import de.twisted.examshare.ui.taskcommon.TaskListMode
import de.twisted.examshare.ui.tasklist.TaskListActivity
import de.twisted.examshare.util.helper.TextUtil

object ActivityHolder {

    private val currentActivity: ExamActivity? = null

    fun redirectIfResumed(activity: ExamActivity, savedInstanceState: Bundle?, data: Uri?): Boolean {
        if (activity is LauncherActivity) return false
        val intent = Intent(activity, LauncherActivity::class.java)
        intent.putExtra("data", data)
        intent.putExtra("activity", activity.javaClass.canonicalName)
        intent.putExtra("savedInstanceState", savedInstanceState)
        intent.putExtra("avoidLogin", activity is ResetPasswordActivity)
        activity.startActivity(intent)
        activity.finish()
        return true
    }

    @JvmStatic
    fun setOptionsListener(activity: ExamActivity, menuItem: MenuItem) {
        when (menuItem.itemId) {
            android.R.id.home -> activity.onBackPressed()
            R.id.menu_item_user_account -> activity.startActivity(Intent(activity, AccountActivity::class.java))
            R.id.menu_item_my_tasks -> activity.startActivity(TaskListActivity.newIntent(activity, TaskListMode.TASKS))
            R.id.menu_item_favorites -> activity.startActivity(TaskListActivity.newIntent(activity, TaskListMode.FAVORITES))
            R.id.menu_item_settings -> activity.startActivity(Intent(activity, SettingsActivity::class.java))
            R.id.menu_item_imprint -> activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Preferences.IMPRINT_URL)))
            R.id.menu_item_about -> activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Preferences.INFO_URL)))
        }
    }

    fun changeNotification(key: String?, status: Boolean) {
        val topic = TextUtil.replaceChars(key)
        val messaging = FirebaseMessaging.getInstance()
        if (status) messaging.subscribeToTopic(topic) else messaging.unsubscribeFromTopic(topic)
    }

    @JvmStatic
    fun logout(context: Context) {
        cancelNotifications(context)
        context.stopService(Intent(context, TaskUploadService::class.java))
        context.startActivity(Intent(context, SignInActivity::class.java))
    }

    @JvmStatic
    fun showInAppStore() {
        val appPackageName = currentActivity!!.packageName
        val uri = "https://play.google.com/store/apps/details?id=$appPackageName"
        currentActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    private fun cancelNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}