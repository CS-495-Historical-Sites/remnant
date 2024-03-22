package com.ua.historicalsitesapp.ui.screens
import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale


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
        // Define the actions to include in the app bar
        actions = {
            // Create an icon button with a heart icon
            IconButton(onClick = {  }) {
                Text(
                    text = "+",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                )
            }
        }

    )


}

@Composable
fun HomeMainContent(){
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
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8UHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D",
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
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
                        text = "jacklyn_hill56",
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Tuscaloosa, Alabama",
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp
                    )
                }

            }

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth(),

                ) {
                val image = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8UGVvcGxlfGVufDB8fDB8fHww"

                val screenWidth = LocalConfiguration.current.screenWidthDp
                Box(
                    modifier = Modifier
                        .width(screenWidth.dp)
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )


                }
            }

            // Content (Reactions)
            Row(
                modifier = Modifier.fillMaxWidth()
//

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 38.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(33.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Filled.ChatBubble,
                            contentDescription = "Comment Icon",
                            modifier = Modifier.size(33.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = null,
                            modifier = Modifier.size(33.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 1.dp,
                bottom =4.dp
            )) {

                Row{
                    Text(
                        text = "15 Users have liked this",
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp
                    )
                }

            }



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
