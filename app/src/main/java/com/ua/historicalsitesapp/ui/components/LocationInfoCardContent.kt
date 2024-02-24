package com.ua.historicalsitesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.data.wikidata.constructWikidataImageLink
import com.ua.historicalsitesapp.ui.foreignintents.createGoogleMapsDirectionsIntent
import com.ua.historicalsitesapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInfoCardContent(location: HsLocationComplete, sheetState: SheetState) {
    Box(
        modifier = Modifier.fillMaxSize(),

        ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .then(Modifier.verticalScroll(scrollState))

        ) {
            RenderLocationInfo(location)
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                thickness = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LocationActionItems(location)
            Spacer(modifier = Modifier.height(32.dp))
        }


    }
}


@Composable
private fun RenderLocationInfo(
    location: HsLocationComplete,
) {
    val imageLink = constructWikidataImageLink(location.wikidataImageName, 1000)

    ImageBox(imageLink)
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = location.name,
        style = Typography.headlineMedium
    )
    if (location.shortDescription != null) {
        Text(
            text = location.shortDescription,
            style = Typography.labelMedium
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    if (location.longDescription != null) {
        var isTextExpanded by remember { mutableStateOf(false) }

        Text(
            text = location.longDescription,
            fontSize = 16.sp,
            maxLines = if (isTextExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable(onClick = { isTextExpanded = !isTextExpanded }),
            style = Typography.bodyMedium,
        )
    }
}

@Composable
private fun ImageBox(imageLink: String) {
    var isImageLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .width(600.dp)
            .then(Modifier.height(300.dp))
    ) {
        AsyncImage(
            model = imageLink,
            contentDescription = "An image of the location",
            modifier = Modifier.fillMaxSize(),
            onSuccess = { isImageLoading = false },
        )

        if (isImageLoading) {
            // Placeholder while loading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }
    }
}


@Composable
private fun LocationActionItems(location: HsLocationComplete) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White)
        ) {
            Text("Add To Favorites")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.Add, "Add to favorites ")

        }
        Button(onClick = {
            val intent = createGoogleMapsDirectionsIntent(
                location.latitude,
                location.longitude,
                location.name
            )
            context.startActivity(intent)
        }, colors = ButtonDefaults.buttonColors(contentColor = Color.White)) {
            Text("Route Me")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.Map, "Route me")
        }
    }
}
