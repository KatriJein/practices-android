package com.example.profile.presentation.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.activity.ComponentActivity

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("FullScreenIntentPolicy")
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("name") ?: "Пользователь"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "time_notification_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lesson notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Lesson notification"
                enableVibration(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            Intent(context, ComponentActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Your favorite lesson started!")
            .setContentText("$name, don't miss it!!!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setFullScreenIntent(pendingIntent, true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}