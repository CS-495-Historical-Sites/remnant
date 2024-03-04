package com.ua.historicalsitesapp.ui.foreignintents

import android.content.Intent
import android.net.Uri

fun createGoogleMapsDirectionsIntent(
    lat: Float,
    long: Float,
    placeName: String,
): Intent {
  val gmmIntentUri = Uri.parse("geo:$lat,$long?q=$placeName")
  val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
  mapIntent.setPackage("com.google.android.apps.maps")
  return mapIntent
}
