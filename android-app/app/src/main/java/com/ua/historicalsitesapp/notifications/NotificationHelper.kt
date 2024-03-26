package com.ua.historicalsitesapp.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

//Class does not work for seemingly no reason, will revisit. Until then all notifications will have to initialize
//personal notification channels
class NotificationHelper: Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "Notification_Helper",
            "Channel Name",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}