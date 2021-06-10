package de.twisted.examshare.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.android.DaggerService
import de.twisted.examshare.ExamShareApplication
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.task.TaskRepository
import de.twisted.examshare.util.result.Result
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.net.SocketException
import java.util.*
import javax.inject.Inject

class TaskUploadService : DaggerService() {

    private var disposable: Disposable? = null

    private lateinit var notificationManager: NotificationManager
    private lateinit var taskQueue: MutableMap<Int, Task>

    @Inject
    lateinit var taskRepository: TaskRepository

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        taskQueue = LinkedHashMap()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        registerReceiver(receiver, IntentFilter("CancelUpload"))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val task: Task? = intent.extras?.getParcelable(EXTRA_TASK)
        if (task == null) {
            stopSelf()
        } else {
            handleAddTask(task)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        clearNotifications()
    }

    private fun handleAddTask(task: Task?) {
        if (taskQueue.isEmpty() && task == null) {
            stopForeground(true)
            stopSelf()
            return
        }

        val notificationId = if (task != null) {
            System.currentTimeMillis().toInt()
        } else {
            taskQueue.keys.iterator().next()
        }

        val nextTask: Task = task ?: taskQueue[notificationId]!!

        if (disposable?.isDisposed != true) {
            notificationManager.cancel(notificationId)
            taskQueue.remove(notificationId)
            createTask(nextTask, notificationId)
        } else {
            val builder = NotificationCompat.Builder(this, ExamShareApplication.NOTIFICATION_CHANNEL)
            showWaitingNotification(builder, nextTask.title, notificationId)
            taskQueue[notificationId] = nextTask
        }
    }

    private fun createTask(task: Task, notificationId: Int) {
        val builder = NotificationCompat.Builder(this, ExamShareApplication.NOTIFICATION_CHANNEL)
        showProgressNotification(builder, task.title, notificationId)

        disposable = taskRepository.createTask(task).subscribeBy(
            onSuccess = { onUploadResult(builder, Result.Success(it)) },
            onError = { onUploadResult(builder, Result.Error(it)) }
        )
    }

    @SuppressLint("RestrictedApi")
    private fun onUploadResult(builder: NotificationCompat.Builder, result: Result<Task>) {
        val success = result is Result.Success
        val task = (result as? Result.Success)?.data

        if (disposable?.isDisposed != true) {
            val progressId = System.currentTimeMillis().toInt()

            builder.setContentTitle(getString(if (success) R.string.task_published else R.string.an_error_occurred))
                .setContentIntent(if (task != null) getTaskIntent(task, progressId) else null)
                .setContentText(task?.title ?: getErrorMessage(result as Result.Error))
                .setProgress(0, 0, false).mActions
                .clear()

            notificationManager.notify(progressId, builder.build())

            if (success) {
                val handler = Handler()
                handler.postDelayed({ notificationManager.cancel(progressId) }, 10000)
            }
        }

        cacheDir.listFiles()?.forEach { it?.delete() }

        if (success || !success && disposable?.isDisposed != false) {
            handleAddTask(null)
        } else if (!success && disposable?.isDisposed != true) {
            clearNotifications()
        }
    }

    private fun showProgressNotification(
        builder: NotificationCompat.Builder,
        title: String,
        notificationId: Int
    ) {
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setProgress(100, 0, true)
            .setContentTitle(getString(R.string.task_publishing))
            .setContentText(title)
            .setShowWhen(false)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .addAction(R.drawable.ic_close, getString(R.string.cancel), getCancelIntent(notificationId))

        startForeground(notificationId, builder.build())
    }

    private fun showWaitingNotification(
        builder: NotificationCompat.Builder,
        title: String,
        notificationId: Int
    ) {
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setContentTitle(getString(R.string.wait_for_publishing))
            .setContentText(title)
            .setWhen(previousLong)
            .setShowWhen(false)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .addAction(R.drawable.ic_close, getString(R.string.cancel), getCancelIntent(notificationId))
            .setOngoing(true)

        notificationManager.notify(notificationId, builder.build())
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        clearNotifications()
        stopSelf()
    }

    private fun clearNotifications() {
        val iterator = taskQueue.keys.iterator()
        while (iterator.hasNext()) {
            notificationManager.cancel(iterator.next())
            iterator.remove()
        }

        stopForeground(true)
        disposable?.dispose()
    }

    private val previousLong: Long
        get() {
            var timestamp = System.currentTimeMillis()
            for (key in taskQueue.keys) {
                timestamp = key.toLong()
            }

            return timestamp - 1
        }

    private fun getCancelIntent(notificationId: Int): PendingIntent {
        val intent = Intent("CancelUpload")
        intent.putExtra("notificationId", notificationId)
        return PendingIntent.getBroadcast(this, System.currentTimeMillis().toInt(), intent, 0)
    }

    private fun getTaskIntent(task: Task?, notificationId: Int): PendingIntent {
        val intent = Intent("TaskNotification")
        intent.putExtra("task", task)
        intent.putExtra("notificationId", notificationId)
        return PendingIntent.getBroadcast(this, System.currentTimeMillis().toInt(), intent, 0)
    }

    private fun getErrorMessage(result: Result.Error): String {
        val errorResId = when (result.throwable) {
            is SocketException -> R.string.connection_error
            else -> R.string.processing_error
        }

        return getString(errorResId)
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val notificationId = intent.getIntExtra("notificationId", 0)
            if (taskQueue.containsKey(notificationId)) {
                taskQueue.remove(notificationId)
                notificationManager.cancel(notificationId)
            } else {
                disposable?.dispose()
            }
        }
    }

    companion object {
        private const val EXTRA_TASK = "task"

        fun newInstance(context: Context, task: Task): Intent {
            return Intent(context, TaskUploadService::class.java).apply {
                putExtra(EXTRA_TASK, task)
            }
        }
    }
}