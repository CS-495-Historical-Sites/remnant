package com.ua.historicalsitesapp.ui.components

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getGeofencingClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import com.ua.historicalsitesapp.data.model.map.ClusterItem
import com.ua.historicalsitesapp.geofence.GeofenceBroadcastReceiver
import com.ua.historicalsitesapp.ui.screens.TAG
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun CustomRendererClustering(
    items: List<ClusterItem>,
    onClusterInfoWindowClick: (ClusterItem) -> Unit,
) {
  val configuration = LocalConfiguration.current
  val screenHeight = configuration.screenHeightDp.dp
  val screenWidth = configuration.screenWidthDp.dp
  val clusterManager = rememberClusterManager<ClusterItem>()

  val algorithm =
      NonHierarchicalViewBasedAlgorithm<ClusterItem>(
          screenWidth.value.toInt(),
          screenHeight.value.toInt(),
      )

  algorithm.maxDistanceBetweenClusteredItems = 200

  clusterManager?.setAlgorithm(
      algorithm,
  )

  val renderer =
      rememberClusterRenderer(
          clusterContent = { cluster ->
            ClusterCircle(
                modifier = Modifier.size(40.dp),
                text = "%,d".format(cluster.size),
                color = Color.DarkGray,
            )
          },
          clusterItemContent = {
            ClusterCircle(
                modifier = Modifier.size(18.dp),
                text = "",
                color = Color.Magenta,
            )
          },
          clusterManager = clusterManager,
      )
  SideEffect {
    clusterManager ?: return@SideEffect
    clusterManager.setOnClusterClickListener {
      Log.d(TAG, "Cluster clicked! $it")
      false
    }
    clusterManager.setOnClusterItemClickListener {
      Log.d(TAG, "Cluster item clicked! $it")
      false
    }
    clusterManager.setOnClusterItemInfoWindowClickListener { onClusterInfoWindowClick(it) }
  }
  SideEffect {
    if (clusterManager?.renderer != renderer) {
      clusterManager?.renderer = renderer ?: return@SideEffect
    }
  }

  if (clusterManager != null) {
    Clustering(
        items = items,
        clusterManager = clusterManager,
    )
  }
}

var geofenceList: ArrayList<Geofence> = ArrayList(100)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun GoogleMapsScreen(
    view: MainPageViewModel,
) {
  var showBottomSheet by remember { mutableStateOf(false) }
  var isPlacingLocation by remember { mutableStateOf(false) }
  var showAddLocationDialog by remember { mutableStateOf(false) }
  var selectedLocation: ClusterItem? = null
  val items = remember { mutableStateListOf<ClusterItem>() }

  val onLocationInfoBoxClick: (ClusterItem) -> Unit = { item ->
    showBottomSheet = true // Update the state value
    selectedLocation = item
  }
  val context = LocalContext.current
  val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

  val cameraPositionState = rememberCameraPositionState { CameraPosition.NULL }

  fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
      usersLocation ->
    if (usersLocation != null) {
      val coordinates = LatLng(usersLocation.latitude, usersLocation.longitude)
      val cameraPosition = CameraPosition.fromLatLngZoom(coordinates, 15F)
      cameraPositionState.move(CameraUpdateFactory.newCameraPosition(cameraPosition))

      val historicalLocations = view.getHistoricalLocationNearPoint(coordinates, 50.0f)
      for (location in historicalLocations) {
        val locationId = location.id
        val position = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
        val shortLocationDescription = location.shortDescription ?: ""
        items.add(
            ClusterItem(
                locationId,
                position,
                location.name,
                shortLocationDescription,
                0f,
            ),
        )
      }

      val geofenceLocations = view.getHistoricalLocationNearPoint(coordinates, 0.5f)
      var count = 0
      for (location in geofenceLocations) {
        if (count < 100) {
          geofenceList.add(
              Geofence.Builder()
                  .setRequestId(location.name)
                  .setCircularRegion(
                      location.latitude.toDouble(), location.longitude.toDouble(), 100f)
                  .setNotificationResponsiveness(1000)
                  .setExpirationDuration(Geofence.NEVER_EXPIRE)
                  .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                  .setLoiteringDelay(2500)
                  .build())
        }
        count += 1
      }

      val geofencingClient: GeofencingClient = getGeofencingClient(context)
      val geofencingPendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
      }

      geofencingClient.addGeofences(getGeofencingRequest(), geofencingPendingIntent).run {
        addOnSuccessListener { Log.d("Geofence", "Successfully add geofences") }
        addOnFailureListener {
          Log.d("Geofence", "Request: ${getGeofencingRequest()}, Intent: $geofencingPendingIntent")
        }
      }
    }
  }
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  Scaffold(
      topBar = {
        CenterAlignedTopAppBar(
            title = {},
            navigationIcon = {
              IconButton(
                  onClick = {
                    scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                  }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Open menu")
                  }
            },
            actions = {},
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xfffce4ec),
                    titleContentColor = Color.DarkGray,
                    navigationIconContentColor = Color.DarkGray,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary))
      },
      floatingActionButton = {
        if (isPlacingLocation) {
          ExtendedFloatingActionButton(
              onClick = { showAddLocationDialog = true },
              icon = { Icon(Icons.Filled.AddLocation, "Add Location") },
              text = { Text(text = "Place Location") },
          )
        }
      },
      floatingActionButtonPosition = FabPosition.Center,
      bottomBar = {
        if (showBottomSheet && selectedLocation != null) {
          LocationInfoCard(
              mainPageViewModel = view,
              selectedLocation = selectedLocation!!,
              onDismissRequest = { showBottomSheet = false },
          )
        }
      }) { contentPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
              ModalDrawerSheet {
                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.AddBox, contentDescription = "Add") },
                    label = { Text(text = "Suggest Location") },
                    selected = false,
                    onClick = {
                      isPlacingLocation = true
                      scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                    },
                    modifier =
                        Modifier.padding(contentPadding).padding(PaddingValues(vertical = 8.dp)))
                HorizontalDivider()
              }
            },
            gesturesEnabled = false) {
              Box(modifier = Modifier.padding(contentPadding)) {
                if (showAddLocationDialog) {
                  SuggestLocationForm(
                      onSubmitSuggestion = {
                          name: String,
                          shortDescription: String,
                          image: ByteArray ->
                        val cameraLocation = cameraPositionState.position.target
                        view.sendLocationAddRequest(
                            name = name,
                            lat = cameraLocation.latitude,
                            cameraLocation.longitude,
                            shortDesc = shortDescription,
                            image = image)
                      },
                      onDismiss = {
                        showAddLocationDialog = false
                        isPlacingLocation = false
                      })
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    googleMapOptionsFactory = { GoogleMapOptions().mapId("ed053e0f6a3454e8") },
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings =
                        MapUiSettings(myLocationButtonEnabled = false, mapToolbarEnabled = false),
                    cameraPositionState = cameraPositionState,
                ) {
                  if (isPlacingLocation) {
                    LocationSuggestionMarker(state = cameraPositionState.position)
                  }
                  CustomRendererClustering(
                      items = items,
                      onLocationInfoBoxClick,
                  )
                }
              }
            }
      }
}

fun getGeofencingRequest(): GeofencingRequest {
  return GeofencingRequest.Builder()
      .apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
        addGeofences(geofenceList)
      }
      .build()
}
