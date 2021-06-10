package de.twisted.examshare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // if (ExamShareApplication.isProcessing() || ExamShareApplication.getInstance().isInitialized())
        //     return;
        //
        // try {
        //     PendingIntent.getActivity(context, 0, new Intent(context, LauncherActivity.class), 0).send();
        // } catch (CanceledException e) {
        //     e.printStackTrace();
        // }
    }
}