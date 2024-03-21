package com.ua.historicalsitesapp.geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.ua.historicalsitesapp.util.hasLocationPermission

class GeofenceManager(private val context: Context) {
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    fun addGeofence(geofenceID: String, latLng: LatLng, radius: Float,
                    onSuccessListener: OnSuccessListener<Void>, onFailureListener : OnFailureListener) {
        val geofence = createGeofence(geofenceID, latLng, radius)
        val geofencingRequest = createGeofencingRequest(geofence as Geofence)
        val pendingIntent = getPendingIntent()

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
        } else {
            hasLocationPermission(context)
        }
    }

    private val createGeofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getPendingIntent(): PendingIntent {
        return createGeofencePendingIntent
    }

    private fun createGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofence(geofence)
            .build()
    }

    private fun createGeofence(geofenceID: String, latLng: LatLng, radius: Float): Any {
        return Geofence.Builder()
            .setRequestId(geofenceID)
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(3000)
            .build()
    }
}