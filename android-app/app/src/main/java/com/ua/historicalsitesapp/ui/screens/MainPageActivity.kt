package com.ua.historicalsitesapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.compose.HistoricalSitesAppTheme
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.ua.historicalsitesapp.nav.AppBottomBar
import com.ua.historicalsitesapp.nav.BottomNavigationGraph
import com.ua.historicalsitesapp.ui.components.GoogleMapsScreen
import com.ua.historicalsitesapp.ui.components.LocationScreen
import com.ua.historicalsitesapp.util.hasLocationPermission
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel

class MainPageActivity : ComponentActivity() {
  private lateinit var geofencingClient: GeofencingClient
  override fun onCreate(savedInstanceState: Bundle?) {
    geofencingClient = LocationServices.getGeofencingClient(this)
    super.onCreate(savedInstanceState)

    onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
          override fun handleOnBackPressed() {}
        },
    )

    setContent {
      HistoricalSitesAppTheme {
        // A surface container using the 'background' color from the theme
        MainScreen()
      }
    }
  }
}

const val TAG = "INFO"

@Composable
fun MainScreen() {
  val navController = rememberNavController()
  Scaffold(
      bottomBar = { AppBottomBar(navController = navController) },
  ) // content:
  { paddingValues ->
    BottomNavigationGraph(
        navController = navController,
        modifier = Modifier.padding(paddingValues),
    )
  }
}

@Composable
fun HomeScreen(modifier: Modifier) {
  Surface(
      modifier = modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background,
  ) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }

    LaunchedEffect(Unit) {
      // Check for permissions when the composable is initially launched
      hasPermission = hasLocationPermission(context)
    }

    if (hasPermission) {
      Box(modifier = Modifier.fillMaxSize()) {
        val view = MainPageViewModel(context)

        GoogleMapsScreen(view)
      }
    } else {
      // Show the LocationScreen if permissions are not granted
      LocationScreen {
        // Callback to update the state when permissions are granted
        hasPermission = true
      }
    }
  }
}
