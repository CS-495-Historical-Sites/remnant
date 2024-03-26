package com.ua.historicalsitesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.MarkerState
import com.ua.historicalsitesapp.ui.theme.Typography

@Composable
fun LocationSuggestionMarker(state: CameraPosition) {
  val pinConfig = PinConfig.builder().setBackgroundColor(Color.Magenta.value.toInt()).build()

  val markerState = MarkerState(position = state.target)
  AdvancedMarker(state = markerState, pinConfig = pinConfig)

}
