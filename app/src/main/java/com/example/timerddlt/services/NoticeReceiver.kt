package com.example.timerddlt.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.timerddlt.presentation.MainActivity

class NoticeReceiver : BroadcastReceiver() {
    companion object {
        val NOTIFICATION_ID: String = "notification-id"
        val NOTIFICATION: String = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = NotificationManagerCompat.from(context!!)
        val notification: Notification =
            intent?.getParcelableExtra<Notification>(NOTIFICATION) as Notification

        val intentOpen = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntentOpen =
            PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.contentIntent = pendingIntentOpen

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(id, notification)
    }
}