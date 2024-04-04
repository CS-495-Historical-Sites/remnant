package com.ua.historicalsitesapp.ui.screens
import android.annotation.SuppressLint
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.unit.sp
import com.example.compose.HistoricalSitesAppTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import kotlin.math.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.rounded.Directions
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
import com.ua.historicalsitesapp.ui.components.LocationScreen
import android.util.Log
import android.content.Context
import androidx.compose.ui.Alignment
import com.ua.historicalsitesapp.ui.foreignintents.createGoogleMapsDirectionsIntent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.Sort
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border


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
fun HomeAppBar(displayCount: Int, totalCount: Int, radius: MutableState<Float>, onRadiusChange: (Float) -> Unit) {
    val radiusInMiles = (radius.value / 1.60934f).roundToInt()

    Column {
        TopAppBar(
            title = {
                Column {
                    Text(
                        "Remnant",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Displaying $displayCount locations out of $totalCount within $radiusInMiles miles",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )

                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black, // App Bar background color
                titleContentColor = Color.White, // App Bar title text color
                actionIconContentColor = Color.White // App Bar action icon color
            ),
            actions = {
                var showMenu by remember { mutableStateOf(false) }
                val radiusOptions = listOf(5, 10, 20)

                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier
                        .border(
                            border = BorderStroke(1.dp, Color.White), // Define the border width and color
                            shape = RoundedCornerShape(4.dp) // Define the corner radius for the rounded square
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Filter",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    radiusOptions.forEach { distanceInMiles ->
                        val distanceInKilometers = distanceInMiles * 1.60934f
                        DropdownMenuItem(
                            text = { Text(text = "$distanceInMiles miles") },
                            onClick = {
                            onRadiusChange(distanceInKilometers)
                            showMenu = false
                        })
                    }
                }
            }
        )
    }
}

@Composable
fun SearchBar(searchQuery: MutableState<String>, onSearch: (String) -> Unit) {


    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = {
            searchQuery.value = it
            onSearch(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 42.dp, max = 50.dp)
            .padding(2.dp),
        textStyle = TextStyle(
            fontSize = 11.sp
        ),
        placeholder = { Text("Search locations", fontSize = 11.sp) },
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
        ),
    )

}


@Composable
fun HomeMainContent(locationInfo: HsLocation, distance: Double){
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
                        .padding(bottom = 40.dp)
                ) {
                    Text(
                        text = locationInfo.name,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp
                    )
                    val formattedDistance = String.format("%.1f", distance)
                    Text(
                        text = "$formattedDistance Miles away",
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp
                    )
                }

            }


            Divider(modifier = Modifier.padding(horizontal = 6.dp), color = Color.Gray)

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
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
fun FeedPage(view: MainPageViewModel, context: Context) {
    val searchRadius = remember { mutableStateOf(16.09f) }
    val searchQuery = remember { mutableStateOf("") }
    val allLocations = remember { mutableStateOf<List<Pair<HsLocation, Double>>>(emptyList()) }
    val displayedLocations = remember { mutableStateOf<List<Pair<HsLocation, Double>>>(emptyList()) }

    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val loadMoreCount = 20

//    fun loadMoreLocations() {
//        val currentCount = displayedLocations.value.size
//        val nextCount = min(currentCount + loadMoreCount, allLocations.value.size)
//        displayedLocations.value = allLocations.value.subList(0, nextCount)
//    }
    val filteredLocations = allLocations.value.filter {
        it.first.name.contains(searchQuery.value, ignoreCase = true)
    }
//    fun loadMoreLocations() {
//        val currentCount = displayedLocations.value.size
//        val locationsToDisplay = if (searchQuery.value.isEmpty()) {
//            allLocations.value
//        } else {
//            filteredLocations
//        }
//        val nextCount = min(currentCount + loadMoreCount, locationsToDisplay.size)
//        displayedLocations.value = locationsToDisplay.take(nextCount)
//    }

//    fun loadMoreLocations(loadMore: Boolean = false) {
//        val currentCount = if (loadMore) displayedLocations.value.size else 0
//        val locationsToDisplay = if (searchQuery.value.isEmpty()) {
//            allLocations.value
//        } else {
//            filteredLocations
//        }
//        val nextCount = min(currentCount + loadMoreCount, locationsToDisplay.size)
//        displayedLocations.value = locationsToDisplay.take(nextCount)
//    }

    fun loadMoreLocations(loadMore: Boolean = false) {
        val currentCount = if (loadMore) displayedLocations.value.size else 0
        val locationsToDisplay = if (searchQuery.value.isEmpty()) {
            allLocations.value
        } else {
            filteredLocations
        }
        val nextCount = min(currentCount + loadMoreCount, locationsToDisplay.size)
        displayedLocations.value = locationsToDisplay.take(nextCount)
    }


    LaunchedEffect(Unit) {
        loadMoreLocations(loadMore = false)
    }

    LaunchedEffect(searchRadius.value) {
        isLoading.value = true
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { usersLocation ->
                    if (usersLocation != null) {
                        val coordinates = LatLng(usersLocation.latitude, usersLocation.longitude)
                        val fetchedLocations = view.getHistoricalLocationNearPoint(coordinates, searchRadius.value)
                        val locationsWithDistances = fetchedLocations.map { location ->
                            Pair(location, calculateDistanceInMiles(
                                usersLocation.latitude,
                                usersLocation.longitude,
                                location.latitude,
                                location.longitude))
                        }.sortedBy {it.second}
                        allLocations.value = locationsWithDistances
                        loadMoreLocations(loadMore = false)
                        isLoading.value = false
                    } else {
                        isLoading.value = false
                        errorMessage.value = "User location not available"
                    }
                }
                .addOnFailureListener { e ->
                    errorMessage.value = "Failed to fetch user location: ${e.message}"
                }
        } catch (e: Exception) {
            errorMessage.value = "Exception in fetching location: ${e.message}"
        }
    }
    Log.d("FeedPage", "display locations ${displayedLocations.value.size}")
    Log.d("FeedPage", "all locations ${allLocations.value.size}")
    Log.d("FeedPage", "Loading Value ${isLoading.value}")

    val displayedCount = displayedLocations.value.size
    val totalCount = allLocations.value.size



    Scaffold(
        topBar = { HomeAppBar(
            displayCount = displayedCount,
            totalCount = totalCount,
            radius = searchRadius,
            onRadiusChange =  { newRadius ->
                searchRadius.value = newRadius
            }) },
    ) { paddingValues ->
        Column (
            modifier = Modifier.padding(paddingValues)
        ){

            SearchBar(searchQuery = searchQuery, onSearch = { searchText ->
                loadMoreLocations()
            })

            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
                else if (displayedLocations.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No results found", Modifier.padding(16.dp))
                    }
            } else {
                displayedLocations.value.let { locationsWithDistances ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize()
                    ) {
                        items(locationsWithDistances) { (location, distance) ->
                            HomeMainContent(location, distance)
                        }
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                if (displayedLocations.value.size < allLocations.value.size) {
                                    Button(
                                        onClick = { loadMoreLocations(loadMore = true) },
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 2.dp)
                                            .fillMaxWidth(),
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                    ) {
                                        Text(
                                            "Load More",
                                            color = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun calculateDistanceInMiles(lat1: Double, lon1: Double, lat2: Float, lon2: Float): Double {
    val earthRadius = 3958.8 // Earth radius in miles

    val dLat = Math.toRadians((lat2 - lat1))
    val dLon = Math.toRadians((lon2 - lon1))

    val originLat = Math.toRadians(lat1)
    val destinationLat = Math.toRadians(lat2.toDouble())

    val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(originLat) * cos(destinationLat)
    val c = 2 * asin(sqrt(a))

    return earthRadius * c
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


