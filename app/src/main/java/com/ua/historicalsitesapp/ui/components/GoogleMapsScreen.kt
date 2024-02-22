package com.ua.historicalsitesapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.ua.historicalsitesapp.data.model.map.ClusterItem
import com.ua.historicalsitesapp.ui.screens.TAG
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel


@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun CustomRendererClustering(
    items: List<ClusterItem>,
    onClusterInfoWindowClick: (ClusterItem) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val clusterManager = rememberClusterManager<ClusterItem>()

    val algorithm =
        NonHierarchicalViewBasedAlgorithm<ClusterItem>(
            screenWidth.value.toInt(),
            screenHeight.value.toInt()
        )

    algorithm.maxDistanceBetweenClusteredItems = 200

    clusterManager?.setAlgorithm(
        algorithm
    )

    val renderer = rememberClusterRenderer(
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

@Composable
fun GoogleMapsScreen(view: MainPageViewModel, items: List<ClusterItem>) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedLocation: ClusterItem? = null

    val onLocationInfoBoxClick: (ClusterItem) -> Unit = { item ->
        showBottomSheet = true // Update the state value
        selectedLocation = item
    }

    Scaffold { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
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

        if (showBottomSheet && selectedLocation != null) {
            LocationInfoCard(
                mainPageViewModel = view,
                selectedLocation = selectedLocation!!,
                onDismissRequest = {
                    showBottomSheet = false
                })

        }
    }
}

