package com.ua.historicalsitesapp.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent!!.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode )
            Log.e("Geofence", errorMessage)
            return
        }
        val transitionType = geofencingEvent.geofenceTransition
        if (transitionType == Geofence.GEOFENCE_TRANSITION_DWELL) {
            val inputData = Data.Builder()
                .putInt("transitionType", transitionType)
                .build()

            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<GeofenceNotificationWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context!!).enqueue(oneTimeWorkRequest)
        } else {
            Log.d("Geofence", "onReceive: Triggering geofence not found")
        }
    }
}