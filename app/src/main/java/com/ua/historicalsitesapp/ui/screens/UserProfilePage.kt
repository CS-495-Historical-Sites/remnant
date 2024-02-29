package com.ua.historicalsitesapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose.HistoricalSitesAppTheme
import com.ua.historicalsitesapp.viewmodels.AuthViewModel


class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserProfilePage()
                }
            }
        }
    }
}

@Composable
fun UserProfilePage(modifier: Modifier = Modifier) {
    val showLogoutConfirmation = remember { mutableStateOf(false) }
    val currentContext = LocalContext.current
    val view = AuthViewModel(currentContext)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This is the user page",
            modifier = Modifier.weight(1f)
        )
        LogoutButton(
            onClick = { showLogoutConfirmation.value = true }
        )
    }

    if (showLogoutConfirmation.value) {
        AlertDialog(

            onDismissRequest = { showLogoutConfirmation.value = false },
            title = {
                Text(
                    text = "Confirm",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            },
            text = { Text("Are you sure you want to logout?") },

            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmation.value = false
                        val logoutResult = view.performLogout()
                        if (logoutResult) {
                            val intent = Intent(currentContext, LoginActivity::class.java)
                            currentContext.startActivity(intent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Black
                    ),

                    ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showLogoutConfirmation.value = false },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Black
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Black

        ),

        content = { Text("Logout") }
    )
}





