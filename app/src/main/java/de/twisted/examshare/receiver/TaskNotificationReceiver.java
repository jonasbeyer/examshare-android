package de.twisted.examshare.receiver;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent broadcastIntent) {
        // Task task = broadcastIntent.getParcelableExtra("task");
        // if (ExamShareApplication.isProcessing())
        //     return;
        //
        // Intent intent;
        // ExamActivity activity = ActivityHolder.getCurrentActivity();
        // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationManager.cancel(broadcastIntent.getIntExtra("notificationId", 0));
        // if (!ExamShareApplication.getInstance().getAuthManager().isSessionAvailable())
        //     return;
        //
        // if (!ExamShareApplication.getInstance().isInitialized()) {
        //     intent = new Intent(context, LauncherActivity.class);
        //     intent.putExtra("activity", TaskDetailsActivity.class.getCanonicalName());
        //     intent.putExtra("subject", task.getSubject());
        //     intent.putExtra("savedInstanceState", new BundleBuilder().putParcelable("task", task).build());
        //     startPendingIntent(context, intent);
        // } else {
        //     intent = new Intent(context, TaskDetailsActivity.class);
        //     intent.putExtra("task", task);
        //     intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //     activity.notifyTaskChange(task, UpdateStatus.ADDED, ListMode.SUBJECT);
        //     startPendingIntent(context, intent);
        // }
    }

    private void startPendingIntent(Context context, Intent intent) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }
}