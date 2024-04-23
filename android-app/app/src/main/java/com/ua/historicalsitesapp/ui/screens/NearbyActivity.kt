package com.ua.historicalsitesapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.compose.HistoricalSitesAppTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.map.*
import com.ua.historicalsitesapp.ui.components.LocationScreen
import com.ua.historicalsitesapp.ui.foreignintents.createGoogleMapsDirectionsIntent
import com.ua.historicalsitesapp.util.hasLocationPermission
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel
import kotlin.math.*
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.text.font.FontStyle


class FeedPageActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HistoricalSitesAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          CheckScreen(Modifier.fillMaxSize())
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    displayCount: Int,
    totalCount: Int,
    radius: MutableState<Float>,
    onRadiusChange: (Float) -> Unit
) {
  val radiusInMiles = (radius.value / 1.60934f).roundToInt()

  Column {
    TopAppBar(
        title = {
          Column {
            Text(
                "Remnant",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 32.sp)
            Text(
                "Displaying $displayCount locations out of $totalCount within $radiusInMiles miles",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                lineHeight = 8.sp,
                )
          }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White),
        actions = {
          var showMenu by remember { mutableStateOf(false) }
          val radiusOptions = listOf(5, 10, 25, 50)

          IconButton(
              onClick = { showMenu = !showMenu },
              modifier =
                  Modifier.border(
                      border = BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(4.dp))) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Filter",
                    tint = Color.White)
              }

          DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
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
        })
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
      modifier =
          Modifier.fillMaxWidth()
              .heightIn(min = 8.dp, max = 65.dp)
              .padding(horizontal = 4.dp, vertical = 5.dp),
      textStyle = TextStyle(fontSize = 16.sp),
      placeholder = { Text("Search locations", fontSize = 16.sp) },
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
fun HomeMainContent(locationInfo: HsLocation, distance: Double, view: MainPageViewModel) {
  val imageLink = locationInfo.imageLink
  val cornerRadius = 8.dp
  val context = LocalContext.current
  var isLiked by remember { mutableStateOf(locationInfo.isLiked) }
  var userHasInteracted by remember { mutableStateOf(false) }
  var isExpanded by remember { mutableStateOf(false) }
  val longDescription = locationInfo.longDescription ?: ""
  val words = longDescription.split("\\s+".toRegex())
  val wordCount = words.filter { it.isNotEmpty() }.size
  val needExpansion = wordCount > 30
  Log.d("Expansion", "${locationInfo.name} description length = ${longDescription.length} expansion = ${needExpansion}")

  LaunchedEffect(isLiked, userHasInteracted) {
    if (userHasInteracted) {
      Log.d("LikedLocations", "Attempting to use launched effect for ${locationInfo.name}")
      val success =
          if (isLiked) {
            view.markLocationAsVisited(locationInfo.id)
          } else {
            view.removeLocationFromVisited(locationInfo.id)
          }

      if (success) {
        Log.d("LikedLocations", "${locationInfo.name} succeeded")
        Toast.makeText(
                context,
                if (isLiked) "Location added to Liked Locations"
                else "Location removed from Liked Locations",
                Toast.LENGTH_SHORT)
            .apply {
              setGravity(Gravity.CENTER, 0, 0)
              show()
            }
      } else {
        Log.d("LikedLocations", "${locationInfo.name} failed")
        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).apply {
          setGravity(Gravity.CENTER, 0, 0)
          show()
        }
      }
    }
    userHasInteracted = false
  }

  Surface(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)) {
    Column {
      Row(
          Modifier.fillMaxWidth().padding(start = 6.dp, end = 6.dp, bottom = 8.dp),
          verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(2f).padding(end = 16.dp)) {
              AsyncImage(
                  model = imageLink,
                  contentDescription = null,
                  modifier =
                      Modifier.fillMaxWidth()
                          .aspectRatio(1f)
                          .width(76.dp)
                          .height(100.dp)
                          .clip(RoundedCornerShape(cornerRadius))
                          .clickable(onClick = {}),
                  contentScale = ContentScale.Crop)
            }

            Column(Modifier.weight(4f).animateContentSize()) {
              Text(text = locationInfo.name,
                  fontWeight = FontWeight.Normal,
                  fontSize = 20.sp,
                  lineHeight = 22.sp)

              val formattedDistance = String.format("%.1f", distance + 1.0)
              Text(
                  text = "$formattedDistance Miles away",
                  fontWeight = FontWeight.Normal,
                  fontStyle = FontStyle.Italic,
                  fontSize = 12.sp,
                  lineHeight = 20.sp)
                if(locationInfo.longDescription != null) {
                    Text(
                        text = locationInfo.longDescription,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp,
                        fontSize = 9.sp,
                        maxLines = if (isExpanded || !needExpansion) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (needExpansion) {
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier
                                .size(22.dp)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }
          }

      HorizontalDivider(modifier = Modifier.padding(horizontal = 6.dp), color = Color.Gray)

      Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
              Box(
                  modifier =
                      Modifier.weight(1f)
                          .clickable(
                              onClick = {
                                userHasInteracted = true
                                isLiked = !isLiked
                              })
                          .height(40.dp),
                  contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector =
                            if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null)
                  }

              Divider(
                  color = Color.Gray,
                  modifier = Modifier.width(1.dp).fillMaxHeight().padding(vertical = 4.dp))

              Box(
                  modifier =
                      Modifier.weight(1f)
                          .clickable(
                              onClick = {
                                val intent =
                                    createGoogleMapsDirectionsIntent(
                                        locationInfo.latitude,
                                        locationInfo.longitude,
                                        locationInfo.name,
                                    )
                                context.startActivity(intent)
                              })
                          .height(40.dp),
                  contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Rounded.Directions, contentDescription = null)
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

  val filteredLocations =
      allLocations.value.filter { it.first.name.contains(searchQuery.value, ignoreCase = true) }

  fun loadMoreLocations(loadMore: Boolean = false) {
    val currentCount = if (loadMore) displayedLocations.value.size else 0
    val locationsToDisplay =
        if (searchQuery.value.isEmpty()) {
          allLocations.value
        } else {
          filteredLocations
        }
    val nextCount = min(currentCount + loadMoreCount, locationsToDisplay.size)
    displayedLocations.value = locationsToDisplay.take(nextCount)
  }

  LaunchedEffect(Unit) { loadMoreLocations(loadMore = false) }

  LaunchedEffect(searchRadius.value) {
    isLoading.value = true
    try {
      val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
      fusedLocationClient
          .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
          .addOnSuccessListener { usersLocation ->
            if (usersLocation != null) {
              val coordinates = LatLng(usersLocation.latitude, usersLocation.longitude)
              val fetchedLocations =
                  view.getHistoricalLocationNearPoint(coordinates, searchRadius.value)
              val locationsWithDistances =
                  fetchedLocations
                      .map { location ->
                        Pair(
                            location,
                            calculateDistanceInMiles(
                                usersLocation.latitude,
                                usersLocation.longitude,
                                location.latitude,
                                location.longitude))
                      }
                      .sortedBy { it.second }
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
      topBar = {
        HomeAppBar(
            displayCount = displayedCount,
            totalCount = totalCount,
            radius = searchRadius,
            onRadiusChange = { newRadius -> searchRadius.value = newRadius })
      },
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
      SearchBar(searchQuery = searchQuery, onSearch = { searchText -> loadMoreLocations() })

      if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          CircularProgressIndicator()
        }
      } else if (displayedLocations.value.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text("No results found", Modifier.padding(16.dp))
        }
      } else {
        displayedLocations.value.let { locationsWithDistances ->
          LazyColumn(modifier = Modifier.padding(4.dp).fillMaxSize()) {
            items(locationsWithDistances) { (location, distance) ->
              HomeMainContent(location, distance, view)
            }
            item {
              Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    if (displayedLocations.value.size < allLocations.value.size) {
                      Button(
                          onClick = { loadMoreLocations(loadMore = true) },
                          modifier =
                              Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                          shape = RoundedCornerShape(50),
                          colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
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
  val earthRadius = 3958.8

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

    LaunchedEffect(Unit) { hasPermission = hasLocationPermission(context) }

    if (hasPermission) {
      Box(modifier = Modifier.fillMaxSize()) {
        val view = MainPageViewModel(context)
        FeedPage(view = view, context = context)
      }
    } else {
      LocationScreen { hasPermission = true }
    }
  }
}
