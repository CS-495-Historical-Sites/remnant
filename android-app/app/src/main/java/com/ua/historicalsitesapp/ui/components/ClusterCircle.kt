package com.ua.historicalsitesapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ClusterCircleGrouping(
    color: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
  Surface(
      modifier,
      shape = CircleShape,
      color = color,
      contentColor = Color.White,
      border = BorderStroke(1.dp, Color.White),
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
          text,
          fontSize = 16.sp,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
fun ClusterCircle(
    imageLink: String,
    modifier: Modifier = Modifier,
) {
  var imageLinkToUse = imageLink
  if (!imageLink.startsWith("https://remnantphotos.s3.") && imageLink != "") {
    imageLinkToUse += "&width=200"
  }

  Surface(
      modifier = modifier,
      shape = CircleShape,
  ) {
    var isImageLoading by remember { mutableStateOf(true) }
    val request =
        ImageRequest.Builder(LocalContext.current).data(imageLinkToUse).allowHardware(false).build()

    AsyncImage(
        model = request,
        contentDescription = "An image of the location",
        contentScale = ContentScale.Crop, // Ensures the image fills the circle and is cropped
        modifier = Modifier.clip(CircleShape), // Clips the image to a circle
        onSuccess = { isImageLoading = false },
        onError = { println(it) })
  }
}
