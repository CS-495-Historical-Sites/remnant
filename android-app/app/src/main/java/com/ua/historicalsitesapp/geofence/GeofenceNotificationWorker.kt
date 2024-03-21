package com.ua.historicalsitesapp.geofence

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.Geofence
import com.ua.historicalsitesapp.notifications.NotificationHelper
import com.ua.historicalsitesapp.ui.screens.MainPageActivity

class GeofenceNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val transitionType = inputData.getInt("TransitionType", -1)
        val notificationHelper = NotificationHelper(applicationContext)
        val title: String
        val message: String

        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                title = "Dwelling in geofence"
                message = "You're dwelling in a geofence"
            }
            else -> {
                title = "Unknown"
                message = "Unknown"
            }
        }

        notificationHelper.sendHighNotification(
            title, message, MainPageActivity::class.java
        )

        return Result.success()
    }
}