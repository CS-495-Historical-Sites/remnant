package com.ua.historicalsitesapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.HistoricalSitesAppTheme
import com.ua.historicalsitesapp.ui.theme.Typography
import com.ua.historicalsitesapp.viewmodels.AuthViewModel
import com.ua.historicalsitesapp.viewmodels.UserProfileViewModel

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    UserProfilePage()
                }
            }
        }
    }
}
// Do email on user profile page
// Maybe do notifications like an on/off button like those switch bars user location
// Questionnaire on user profile page
@Composable
fun UsernameTextField(
    initialValue: String,
    fetchedUsername: String,
    onUserNameChange: (String) -> Unit,
    onUsernameClick: () -> Unit,
    isEditing: Boolean
) {
    var text by remember { mutableStateOf(initialValue) }

    Box(
        modifier = Modifier.clickable {
            onUsernameClick()
        },
        content = {
            if (isEditing) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        onUserNameChange(newText)
                    },
                    label = { Text("Change Username?") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = fetchedUsername,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    )
}


@Composable
private fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    if (isVisible) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth().height(56.dp),
        ) {
            Text(
                "Save",
                style = Typography.labelLarge,
            )
        }
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(50.dp),
        colors =
        ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Black,
        ),
        content = { Text("Logout") },
    )
}

@Composable
fun UserProfileCard(
    modifier: Modifier = Modifier,
    fetchedUsername: String,
    fetchedEmail: String,
    onUsernameChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 400.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .align(Alignment.TopStart),
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column( // Username and Save button
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    // Username
                    UsernameTextField(
                        initialValue = "Initial Value", // You can set an initial value here if needed
                        fetchedUsername = fetchedUsername,
                        onUserNameChange = onUsernameChange,
                        onUsernameClick = { isEditing = true }, // Update isEditing state when username is clicked
                        isEditing = isEditing // When save button is clicked disable the text box
                    )
                    // Save button
                    SaveButton(
                        onClick = {
                            onSaveClick()
                            isEditing = false // Set isEditing to false after save button is clicked
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isVisible = isEditing
                    )
                    // Email
                    Text(
                        text = fetchedEmail,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                    )
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
    val userView = UserProfileViewModel(currentContext)
    var username by remember { mutableStateOf("") }
    var fetchedUsername by remember { mutableStateOf("") }
    val email by remember { mutableStateOf("") }
    var fetchedEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchedUsername = userView.getUsername(username).username
        fetchedEmail = userView.getEmail(email).email
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserProfileCard(
            modifier = Modifier.weight(1f),
            fetchedUsername = fetchedUsername,
            fetchedEmail = fetchedEmail,
            onUsernameChange = { username = it },
            onSaveClick = {
                userView.updateUsername(username)

            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            LogoutButton(
                onClick = { showLogoutConfirmation.value = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showLogoutConfirmation.value) {
                AlertDialog(
                    onDismissRequest = { showLogoutConfirmation.value = false },
                    title = {
                        Text(
                            text = "Confirm",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
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
                                containerColor = Color.Black,
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
                                containerColor = Color.Black,
                            ),
                        ) {
                            Text("Cancel")
                        }
                    },
                )
            }
        }
    }
}





