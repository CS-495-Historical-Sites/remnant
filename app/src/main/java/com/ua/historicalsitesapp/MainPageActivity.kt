package com.ua.historicalsitesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ua.historicalsitesapp.data.model.Location
import com.ua.historicalsitesapp.data.model.UserFavoriteLocationDeleteRequest
import com.ua.historicalsitesapp.ui.theme.HistoricalSitesAppTheme

class MainPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun LocationCard(location: Location, favoriteLocationDeleteRequest: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Text(
            text = location.name,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        IconButton(onClick = favoriteLocationDeleteRequest) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete Favorite Location")
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val view = MainPageViewModel()
    val favoriteLocations = view.getUsersFavoriteLocations()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Hello user, your favorite locations are..",
            modifier = modifier
        )


        for (location in favoriteLocations) {
            LocationCard(location) {
                view.deleteUserFavoriteLocation(
                    UserFavoriteLocationDeleteRequest(location.id)
                )
            }
        }
    }

}

