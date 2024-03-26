package com.ua.historicalsitesapp.ui.components

import android.content.Context
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.ui.foreignintents.createGoogleMapsDirectionsIntent
import com.ua.historicalsitesapp.ui.theme.Typography
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel

@Composable
fun LocationInfoCardContent(
    location: HsLocationComplete,
) {
  val view = MainPageViewModel(LocalContext.current)
  var showEditForm by remember { mutableStateOf(false) }
  Box(
      modifier = Modifier.fillMaxSize(),
  ) {
    if (showEditForm) {
      SuggestEditForm(
          location = location,
          onSubmitSuggestion = { title, shortDescription, longDescription ->
            view.sendLocationEditSuggestionRequest(
                locationId = location.id,
                name = title,
                shortDesc = shortDescription,
                longDesc = longDescription)
            showEditForm = false
          },
          onDismiss = { showEditForm = false })
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.padding(horizontal = 32.dp).then(Modifier.verticalScroll(scrollState)),
    ) {
      RenderLocationInfo(location) { showEditForm = true }
      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(
          thickness = 3.dp,
      )
      Spacer(modifier = Modifier.height(16.dp))
      LocationActionItems(location)
      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
private fun RenderLocationInfo(location: HsLocationComplete, onEditClick: () -> Unit) {
  val imageLink = location.imageLink + "&width=1000"
  ImageBox(imageLink)
  Spacer(modifier = Modifier.height(6.dp))
  TitleBox(locationName = location.name, onEditClick = onEditClick)
  if (location.shortDescription != null) {
    Text(
        text = location.shortDescription,
        style = Typography.labelMedium,
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
private fun TitleBox(locationName: String, onEditClick: () -> Unit) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(text = locationName, style = Typography.headlineMedium, modifier = Modifier.weight(1f))
    IconButton(onClick = { onEditClick() }) {
      Icon(Icons.Filled.Edit, contentDescription = "Suggest Edit")
    }
  }
}

@Composable
private fun ImageBox(imageLink: String) {
  var isImageLoading by remember { mutableStateOf(true) }
  Box(
      modifier = Modifier.width(600.dp).then(Modifier.height(300.dp)),
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
          modifier = Modifier.fillMaxSize().background(Color.LightGray),
      )
    }
  }
}

@Composable
private fun LocationActionItems(location: HsLocationComplete) {
  val context = LocalContext.current
  val view = MainPageViewModel(context)
  Column {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(4.dp),
    ) {
      ToggleVisitedButton(view = view, location = location)
      RouteMeButton(context = context, location = location)
    }
  }
}

@Composable
private fun RouteMeButton(context: Context, location: HsLocationComplete) {
  Button(
      onClick = {
        val intent =
            createGoogleMapsDirectionsIntent(
                location.latitude,
                location.longitude,
                location.name,
            )
        context.startActivity(intent)
      },
      colors = ButtonDefaults.buttonColors(contentColor = Color.White)) {
        Text("Route Me")
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.Map, "Route me")
      }
}

@Composable
private fun ToggleVisitedButton(view: MainPageViewModel, location: HsLocationComplete) {
  var hasVisitedLocation by remember { mutableStateOf(view.hasUserVisitedLocation(location.id)) }
  Button(
      onClick = {
        if (!hasVisitedLocation) {
          val successfullyMarked = view.markLocationAsVisited(location.id)
          if (successfullyMarked) {
            hasVisitedLocation = true
          }
        } else {
          val successfullyRemoved = view.removeLocationFromVisited(location.id)
          if (successfullyRemoved) {
            hasVisitedLocation = false
          }
        }
      },
      colors = ButtonDefaults.buttonColors(contentColor = Color.White),
  ) {
    val buttonText =
        if (hasVisitedLocation) {
          "Remove from visits"
        } else {
          "Add to visits"
        }

    Text(buttonText)
    Spacer(modifier = Modifier.width(4.dp))
    Icon(Icons.Default.Add, buttonText)
  }
}
