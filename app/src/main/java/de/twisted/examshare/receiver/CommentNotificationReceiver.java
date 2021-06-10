package de.twisted.examshare.receiver;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CommentNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent broadcastIntent) {
        // Task task = broadcastIntent.getParcelableExtra("task");
        // Comment comment = broadcastIntent.getParcelableExtra("comment");
        // if (ExamShareApplication.isProcessing())
        //     return;
        //
        // Intent intent;
        // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationManager.cancel(broadcastIntent.getIntExtra("notificationId", 0));
        // if (!ExamShareApplication.getInstance().getAuthManager().isSessionAvailable())
        //     return;
        //
        // Bundle bundle = new BundleBuilder()
        //         .putParcelable("task", task)
        //         .putParcelable("comment", comment)
        //         .build();
        //
        // if (!ExamShareApplication.getInstance().isInitialized()) {
        //     intent = new Intent(context, LauncherActivity.class);
        //     intent.putExtra("activity", CommentDetailsActivity.class.getCanonicalName());
        //     intent.putExtra("subject", task.getSubject());
        //     intent.putExtra("task", task);
        //     intent.putExtra("savedInstanceState", bundle);
        //     startPendingIntent(context, intent);
        // } else {
        //     intent = new Intent(context, CommentDetailsActivity.class);
        //     intent.putExtras(bundle);
        //     intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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