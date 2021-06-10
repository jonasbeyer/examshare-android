package de.twisted.examshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import de.twisted.examshare.di.DaggerAppComponent
import timber.log.Timber

class ExamShareApplication : DaggerApplication() {

    companion object {
        const val NOTIFICATION_CHANNEL = "general_notifications"
    }

    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    // public void login(Activity activity, boolean init, Consumer<ExResponse> consumer) {
//     this.userProfile = new User(httpClient, authManager.getSessionId());
//     this.userProfile.loadData(init, (response) -> {
//         if (response.isUnavailable(init)) return;
//         if (response.getType() != ResponseType.SUCCESS) {
//             consumer.accept(response);
//             return;
//         }
//         subjectManager.loadSubjects(init, (responseType) -> {
//             if (responseType != ResponseType.SUCCESS) {
//                 consumer.accept(new ExResponse(responseType));
//                 return;
//             }
//             consumer.accept(null);
//             this.updateFirebaseInstance();
//             this.startInitActivity(activity);
//             this.setTransition(activity, init);
//         });
//     });
// }
//    fun startInitActivity(activity: Activity) {
//        if (!activity.intent.hasExtra("activity")) {
//            val intent = Intent(activity, MainActivity::class.java)
//            activity.startActivity(intent)
//            activity.finish()
//            return
//        }
//        val extras = activity.intent.extras
//        val intent = Intent()
//        intent.putExtra("data", extras!!.getParcelable("data") as Parcelable?)
//        intent.putExtra("savedInstanceState", extras.getBundle("savedInstanceState"))
////        intent.setClassName(activity, extras.getString("activity"))
//        if (extras.containsKey("subject")) {
//            val stackBuilder = TaskStackBuilder.create(activity)
//            stackBuilder.addNextIntentWithParentStack(intent)
//            stackBuilder.editIntentAt(1).putExtra("subject", extras.getString("subject"))
//            if (extras.containsKey("task")) stackBuilder.editIntentAt(2).putExtra("task", extras.getParcelable<Parcelable>("task") as Task?)
//            stackBuilder.startActivities()
//            activity.finish()
//            return
//        }
//        activity.startActivity(intent)
//        activity.finish()
//    }

//    fun setTransition(activity: Activity, enabled: Boolean) {
//        if (enabled) activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//    }

    // public void updateFirebaseInstance() {
//     FirebaseMessaging messaging = FirebaseMessaging.getInstance();
//     messaging.subscribeToTopic(getString(R.string.app_name));
//     messaging.subscribeToTopic(String.valueOf(userProfile.getUserId()));
//     for (String subject : subjectManager.getSubjects().keySet())
//         ActivityHolder.changeNotification(subject, userProfile.isNotificationEnabled(subject));
// }
    
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NOTIFICATION_CHANNEL
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(NotificationChannel(notificationChannel, getString(R.string.general_notifications), NotificationManager.IMPORTANCE_DEFAULT))
        }
    }
}