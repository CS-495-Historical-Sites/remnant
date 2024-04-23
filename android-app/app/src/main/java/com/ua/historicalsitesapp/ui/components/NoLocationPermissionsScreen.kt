package com.ua.historicalsitesapp.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ua.historicalsitesapp.util.hasLocationPermission
import com.ua.historicalsitesapp.util.hasNotificationPermission

@Composable
fun LocationScreen(onPermissionGranted: () -> Unit) {
  val context = LocalContext.current

  // Create a permission launcher
  val requestPermissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted: Boolean ->
            if (isGranted) {
              onPermissionGranted()
            }
          },
      )

  var hasNotificationPermission =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        hasNotificationPermission(context)
      } else {
        true
      }

  val permissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted -> hasNotificationPermission = isGranted })

  if (!hasNotificationPermission) {
    LaunchedEffect(Unit) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Button(
        onClick = {
          if (!hasLocationPermission(context)) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
          }
        },
    ) {
      Text(text = "Grant location permissions")
    }
    Spacer(modifier = Modifier.height(16.dp))
  }
}
