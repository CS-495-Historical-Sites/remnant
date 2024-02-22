package com.ua.historicalsitesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete

@Composable
fun LocationInfoCardContent(location: HsLocationComplete) {
    val imageLink =
        "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/" + location.wikidataImageName + "&width=600"
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
                text = location.name,
                style = TextStyle(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Summary:",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            if (location.shortDescription != null) {
                Text(
                    text = location.shortDescription,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Details:",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            if (location.longDescription != null) {
                Text(
                    text = location.longDescription,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Text(
                text = "Image",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            AsyncImage(
                model = imageLink,
                contentDescription = "An image of the location",
            )
        }
    }

}