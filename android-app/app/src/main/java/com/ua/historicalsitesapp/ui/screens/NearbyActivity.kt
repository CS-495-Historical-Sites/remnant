package com.ua.historicalsitesapp.ui.screens
import android.annotation.SuppressLint
import android.os.Bundle
import com.ua.historicalsitesapp.data.model.map.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.HistoricalSitesAppTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.wikidata.constructWikidataImageLink
import com.ua.historicalsitesapp.util.hasLocationPermission
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import com.ua.historicalsitesapp.ui.components.GoogleMapsScreen
import com.ua.historicalsitesapp.ui.components.LocationScreen
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import android.content.Context
import androidx.compose.ui.Alignment
import com.ua.historicalsitesapp.ui.foreignintents.createGoogleMapsDirectionsIntent

class FeedPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheckScreen(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(){
    TopAppBar(
        title = {
            Text(
                "Remnant",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        },
        actions = {
        }
    )
}

@Composable
fun HomeMainContent(locationInfo: HsLocation){
    val imageLink = constructWikidataImageLink(locationInfo.wikidataImageName, 40)
    val cornerRadius = 8.dp
    val context = LocalContext.current

    Surface(modifier = Modifier.padding(top=6.dp, bottom = 6.dp)) {


        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .weight(2f)
                        .padding(end = 16.dp)
                ) {
                    AsyncImage(
                        model = imageLink,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .width(76.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(cornerRadius))
                            .clickable(onClick = {}),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    Modifier
                        .weight(6f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = locationInfo.name,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                    Text(
                        text = locationInfo.shortDescription!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp
                    )
                }

            }


            Divider(modifier = Modifier.padding(horizontal = 6.dp), color = Color.Gray)

            // Content (Reactions)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
//
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),

                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = {  })
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = null
                            )
                        }

                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .padding(vertical = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = {val intent =
                                createGoogleMapsDirectionsIntent(
                                    locationInfo.latitude,
                                    locationInfo.longitude,
                                    locationInfo.name,
                                )
                                context.startActivity(intent)})
                            .height(40.dp),
                        contentAlignment = Alignment.Center

                    ) {
                            Icon(
                                imageVector = Icons.Rounded.Directions,
                                contentDescription = null
                            )
                    }
                }
            }
            Divider(modifier = Modifier.padding(horizontal = 6.dp), color = Color.Gray)





        }
    }

}

@SuppressLint("MissingPermission")
@Composable
fun FeedPage(view: MainPageViewModel,context: Context) {
    val listOfLocationsState = remember { mutableStateOf<List<HsLocation>>(emptyList()) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { usersLocation ->
                    if (usersLocation != null) {
                        val coordinates =
                            LatLng(usersLocation.latitude, usersLocation.longitude)
                        val fetchedLocations =
                            view.getHistoricalLocationNearPoint(coordinates, 40.0f)
                        listOfLocationsState.value = fetchedLocations
                    } else {
                        errorMessage.value = "User location not available"
                        Log.d("FeedPage", errorMessage.value!!)
                    }
                    isLoading.value = false
                }
                .addOnFailureListener { e ->
                    errorMessage.value = "Failed to fetch user location: ${e.message}"
                    Log.e("FeedPage", errorMessage.value!!)
                }
        } catch (e: Exception) {
            errorMessage.value = "Exception in fetching location: ${e.message}"
            Log.e("FeedPage", errorMessage.value!!)
        }
    }

    Scaffold(
        topBar = { HomeAppBar() },
    ) { paddingValues ->
        if (isLoading.value) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(.5.dp)
                        .background(Color.Black)
                )
                Column {

                    Log.d("FeedPage", "List $listOfLocationsState")
//                    for (location in listOfLocationsState.value) {
//                        HomeMainContent(location)
//                    }
                    listOfLocationsState.value.take(3).forEach { location ->
                        HomeMainContent(location)
                    }
                }
            }
        }
        if (errorMessage.value != null) {
            Text("Error: ${errorMessage.value}", color = Color.Red)
        }
    }
}


@Composable
fun CheckScreen(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val context = LocalContext.current
        var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }

        LaunchedEffect(Unit) {
            hasPermission = hasLocationPermission(context)
        }

        if (hasPermission) {
            Box(modifier = Modifier.fillMaxSize()) {
                val view = MainPageViewModel(context)
                FeedPage(view = view, context = context)
            }
        } else {
            LocationScreen {
                hasPermission = true
            }
        }
    }
}


