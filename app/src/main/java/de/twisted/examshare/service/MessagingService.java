package de.twisted.examshare.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import de.twisted.examshare.ExamShareApplication;
import de.twisted.examshare.R;
import de.twisted.examshare.util.ImageLibrary;
import de.twisted.examshare.util.Preferences;
import de.twisted.examshare.data.models.Comment;
import de.twisted.examshare.data.models.Task;
import timber.log.Timber;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("Received message payload %s", remoteMessage.getData());

        Notification notification;
        Map<String, String> data = remoteMessage.getData();

        int notificationId = (int) System.currentTimeMillis();
        if (data.containsKey("comment")) {
            Gson gson = new Gson();
            Task task = gson.fromJson(data.get("task"), Task.class);
            Comment comment = gson.fromJson(data.get("comment"), Comment.class);
            notification = buildCommentNotification(comment, task, data, notificationId);
        } else if (data.containsKey("task")) {
            Task task = new Gson().fromJson(data.get("task"), Task.class);
            notification = buildTaskNotification(task, data.get("body"), notificationId);
        } else {
            notification = buildNotification(data);
        }

        if (notification == null)
            return;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int smallIconViewId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        if (smallIconViewId != 0) {
            if (notification.contentView != null)
                notification.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

            if (notification.bigContentView != null)
                notification.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
        }

        manager.notify(notificationId, notification);
    }

    private Notification buildNotification(Map<String, String> data) {
        int userId = getSharedPreferences("Session", 0).getInt("userId", -1);
        if (userId == -1)
            return null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ExamShareApplication.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent("Notification"), 0))
                .setVibrate(Preferences.isVibrationEnabled() ? new long[]{0, 650, 0, 0} : null)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body")))
                .setSound(Preferences.getRingtoneUri());

        return builder.build();
    }

    private Notification buildTaskNotification(Task task, String body, int notificationId) {
        // int userId = getSharedPreferences("Session", 0).getInt("userId", -1);
        // if (userId == -1 || task.getAuthorId() == userId)
        //     return null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ExamShareApplication.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(getString(R.string.new_task_published))
                .setContentText(body)
                .setContentIntent(getTaskIntent(task, notificationId))
                .setVibrate(Preferences.isVibrationEnabled() ? new long[]{0, 650, 0, 0} : null)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setSummaryText(task.getAuthor()))
                .setSound(Preferences.getRingtoneUri());

        return builder.build();
    }

    private Notification buildCommentNotification(Comment comment, Task task, Map<String, String> data, int notificationId) {
        // int userId = getSharedPreferences("Session", 0).getInt("userId", -1);
        // if (userId == -1 || comment.getAuthorId() == userId)
        //     return null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ExamShareApplication.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(getProfileImage(data.get("profileImage")))
                .setContentTitle(getString(R.string.new_comment, comment.getAuthor()))
                .setContentText(data.get("body"))
                .setContentIntent(getCommentIntent(comment, task, notificationId))
                .setVibrate(Preferences.isVibrationEnabled() ? new long[]{0, 650, 0, 0} : null)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body")).setSummaryText(task.getTitle()))
                .setSound(Preferences.getRingtoneUri());

        return builder.build();
    }

    private PendingIntent getTaskIntent(Task task, int notificationId) {
        Intent intent = new Intent("TaskNotification");
        intent.putExtra("task", task);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, 0);
    }

    private PendingIntent getCommentIntent(Comment comment, Task task, int notificationId) {
        Intent intent = new Intent("CommentNotification");
        intent.putExtra("comment", comment);
        intent.putExtra("task", task);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, 0);
    }

    private Bitmap getProfileImage(String url) {
        int width = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        int height = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height);

        Bitmap profileImage = loadBitmapFromUrl(url, width, height);
        return profileImage != null ? profileImage : ImageLibrary.getBitmap(this, R.drawable.ic_account_circle_grey, width, height);
    }

    private Bitmap loadBitmapFromUrl(String imageUrl, int width, int height) {
        FutureTarget<Bitmap> future = Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .apply(new RequestOptions()
                        .optionalCircleCrop()
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .submit();

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}