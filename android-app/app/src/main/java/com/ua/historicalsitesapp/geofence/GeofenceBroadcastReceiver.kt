package com.ua.historicalsitesapp.geofence

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.ua.historicalsitesapp.R
import com.ua.historicalsitesapp.ui.screens.MainPageActivity

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode )
                Log.e("Geofence", errorMessage)
                return
            }
        }
//        val transitionType = geofencingEvent?.geofenceTransition
//        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
//            transitionType == Geofence.GEOFENCE_TRANSITION_DWELL ||
//            transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
//            //val triggeringGeofences : MutableList<Geofence>? = geofencingEvent.triggeringGeofences
//            println("Penis5")
//        }
        if (context != null) {

            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainPageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setContentText("Geofence")
            .setContentTitle("Geofence")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }


}