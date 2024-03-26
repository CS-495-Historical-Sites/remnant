package com.ua.historicalsitesapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.MarkerState

@Composable
fun LocationSuggestionMarker(state: CameraPosition) {
  val pinConfig = PinConfig.builder().setBackgroundColor(Color.Magenta.value.toInt()).build()

  val markerState = MarkerState(position = state.target)
  AdvancedMarker(state = markerState, pinConfig = pinConfig)
}
