package com.ua.historicalsitesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ua.historicalsitesapp.nav.AppBottomBar
import com.ua.historicalsitesapp.nav.BottomNavigationGraph
import com.ua.historicalsitesapp.ui.theme.HistoricalSitesAppTheme

class MainPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HistoricalSitesAppTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) },

        ) //content:
    { paddingValues ->
        BottomNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMapContent(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun GoogleMapContent(modifier: Modifier) {
    val context = LocalContext.current

    val view = MainPageViewModel(context)

    val locations = view.getAllLocations()


    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        for (location in locations) {
            val pos = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
            Marker(
                state = MarkerState(position = pos),
                title = location.name
            )
        }
    }
}