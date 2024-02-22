package com.ua.historicalsitesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
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

val TAG = "INFO"


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
    GoogleMapClustering()
}

data class MyItem(
    val itemId: Int,
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    override fun getZIndex(): Float =
        itemZIndex
}

@Composable
fun GoogleMapClustering() {
    val items = remember { mutableStateListOf<MyItem>() }
    val context = LocalContext.current
    val view = MainPageViewModel(context)
    val locations = view.getAllLocations()

    // TODO: FIX THIS LOOP. MYITEM AND THE LOCATION SHOULD BE ONE
    // actually maybe not. idk
    for (location in locations) {
        val locationId = location.id
        val position = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
        val shortLocationDescription = location.shortDescription ?: ""
        items.add(MyItem(locationId, position, location.name, shortLocationDescription, 0f))
    }


    GoogleMapClustering(mainPageViewModel = view, items = items)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapClustering(mainPageViewModel: MainPageViewModel, items: List<MyItem>) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedLocation: MyItem? = null


    val onLocationInfoBoxClick: (MyItem) -> Unit = { item ->
        showBottomSheet = true // Update the state value
        selectedLocation = item
    }


    Scaffold { contentPadding ->
        // Screen content
        // [START_EXCLUDE silent]
        Box(modifier = Modifier.padding(contentPadding)) {
            val singapore = LatLng(1.35, 103.87)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 6f)
                },
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId("ed053e0f6a3454e8")
                }
            ) {
                CustomRendererClustering(
                    items = items,
                    onLocationInfoBoxClick
                )

            }
        }
        // [END_EXCLUDE]

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                val info = selectedLocation?.let { mainPageViewModel.getLocationInfo(it.itemId) }
                if (info != null) {
                    val name = info.name
                    val shortDesc = info.shortDescription
                    val longDesc = info.longDescription
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Location:",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            )
                            Text(
                                text = name,
                                style = TextStyle(fontSize = 18.sp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Summary:",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            )
                            if (shortDesc != null) {
                                Text(
                                    text = shortDesc,
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Details:",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            )
                            if (longDesc != null) {
                                Text(
                                    text = longDesc,
                                    style = TextStyle(fontSize = 14.sp)
                                )
                            }
                        }
                    }

                }


            }
        }
    }

}

@Composable
private fun CircleContent(
    color: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier,
        shape = CircleShape,
        color = color,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CustomRendererClustering(items: List<MyItem>, onClusterInfoWindowClick: (MyItem) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val clusterManager = rememberClusterManager<MyItem>()

    val algorithm =
        NonHierarchicalViewBasedAlgorithm<MyItem>(
            screenWidth.value.toInt(),
            screenHeight.value.toInt()
        )

    algorithm.maxDistanceBetweenClusteredItems = 200

    clusterManager?.setAlgorithm(
        algorithm
    )

    val renderer = rememberClusterRenderer(
        clusterContent = { cluster ->
            CircleContent(
                modifier = Modifier.size(40.dp),
                text = "%,d".format(cluster.size),
                color = Color.DarkGray,
            )
        },
        clusterItemContent = {
            CircleContent(
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
        clusterManager.setOnClusterItemInfoWindowClickListener {
            onClusterInfoWindowClick(it)
        }
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