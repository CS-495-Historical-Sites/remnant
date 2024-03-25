package com.ua.historicalsitesapp.ui.screens
import android.os.Bundle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.HistoricalSitesAppTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

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
                    FeedPage()
                }
            }
        }
    }
}

//@Composable
//fun Menu(){
//    var expanded by remember { mutableStateOf( false ) }
//    val radius = listOf("5", "10", "25", "50")
//
//    var mSelectedText by remember { mutableStateOf("") }
//
//    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
//
//
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(){
    TopAppBar(
        title = {
            Text(
                "Remnant",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        },
        actions = {


        }

    )


}

@Composable
fun HomeMainContent(){
    val cornerRadius = 8.dp
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
                        model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8UHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D",
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
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Name of Location",
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Miles away",
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "Tuscaloosa, Alabama",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }

            }


            Divider(modifier = Modifier.padding(horizontal = 6.dp), color = Color.Gray)

            // Content (Reactions)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
//
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
                            .clickable(onClick = { /* Click action for the first button */ })
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
                            .clickable(onClick = {})
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





@Composable
fun FeedPage() {
    Scaffold(
        topBar = { HomeAppBar() },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(.5.dp)
                    .background(Color.Black)
            )
            Column {


                //Main Content 1
                HomeMainContent()
                HomeMainContent()
                HomeMainContent()

                HomeMainContent()
                HomeMainContent()
                HomeMainContent()
                HomeMainContent()
                HomeMainContent()
                HomeMainContent()


            }


        }

    }
}

