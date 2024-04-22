package com.ua.historicalsitesapp.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
// button to change questionnaire answers
// button for night mode
// Maybe do notifications like an on/off button like those switch bars user location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
        TopAppBar(
            title = {
                Column {
                    Text(
                        "Remnant",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)

                }
            },
            colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface, // App Bar background color
                titleContentColor = Color.White, // App Bar title text color
                actionIconContentColor = Color.White // App Bar action icon color
            ),
        )
    }

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
      modifier = Modifier.clickable { onUsernameClick() },
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
              colors =
                  OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = MaterialTheme.colorScheme.secondary),
              modifier = Modifier.fillMaxWidth())
        } else {
          Text(
              text = fetchedUsername,
              style =
                  TextStyle(
                      fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),
              )
        }
      }
  )
}

@Composable
private fun SaveButton(onClick: () -> Unit, modifier: Modifier = Modifier, isVisible: Boolean) {
  if (isVisible) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
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
fun Preferences(
    modifier: Modifier = Modifier,
    onPreferencesToggle: () -> Unit,
    isPreferencesExpanded: Boolean,
    fetchedAnswers: Map<String, Set<String>>?,
    onEditClick: () -> Unit // Callback for when the Edit button is clicked
) {
    var preferencesHeight by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.clickable {
            onPreferencesToggle()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.onGloballyPositioned {
                preferencesHeight = it.size.height // Grabs the height of this BoxScope
            }
        ) {
            // Preferences text aligned to the left
            Text(
                text = "Preferences",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )


            Spacer(modifier = Modifier.weight(1f))
            // Arrow icon
            Icon(
                imageVector = if (isPreferencesExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Expand preferences"
            )
            // Edit Icon
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            }

        }
    }

    // Start of animating preferences showing fetchedAnswers
    AnimatedVisibility(
        visible = isPreferencesExpanded,
        enter = slideInVertically { -preferencesHeight + 36 },
        exit = slideOutVertically { -preferencesHeight + 36 }
    ) {
        Box(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
        ) {
            // Display questions and answers when dropdown is expanded
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) // Top horizontal divider
                fetchedAnswers?.forEach { (question, answers) ->
                    Text(text = question, style = TextStyle(fontSize = 16.sp))
                    answers.forEach { answer ->
                        Chip(text = answer)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) // Bottom horizontal divider
            }
        }
    }
}



@Composable
fun Chip(text: String) {
    Surface(
        color = Color(android.graphics.Color.parseColor("#ADC178")), // Change the color to light green
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(4.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ShowEditConfirm(
    showEditConfirmation: MutableState<Boolean>, // Show/Hide
    onConfirm: () -> Unit,

    ) {
    AlertDialog(
        onDismissRequest = { showEditConfirmation.value = false },
        title = {
            Text(
                text = "Confirm",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )
        },
        text = { Text("Are you sure you want to edit your preferences?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    showEditConfirmation.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black
                ),
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showEditConfirmation.value = false
                },
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


@Composable
fun UserProfileCard(
    modifier: Modifier = Modifier,
    fetchedUsername: String,
    fetchedEmail: String,
    fetchedAnswers: Map<String, Set<String>>?,
    onUsernameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isPreferencesExpanded: Boolean,
    onPreferencesToggle: () -> Unit,
    onEditClick: () -> Unit,
    currentContext: Context
) {
    var isEditing by remember { mutableStateOf(false) }
    val showEditConfirmation = remember { mutableStateOf(false) }


    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = modifier.fillMaxSize()
//            Modifier
//                .size(width = 300.dp, height = 400.dp)
//                .clip(shape = RoundedCornerShape(16.dp))
//                .align(Alignment.TopStart),
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    // Username
                    UsernameTextField(
                        initialValue = fetchedUsername, // You can set an initial value here if needed
                        fetchedUsername = fetchedUsername,
                        onUserNameChange = onUsernameChange,
                        onUsernameClick = {
                            isEditing = true
                        }, // Update isEditing state when username is clicked
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
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // Preferences dropdown
                    Preferences(
                        onPreferencesToggle = onPreferencesToggle,
                        isPreferencesExpanded = isPreferencesExpanded,
                        fetchedAnswers = fetchedAnswers,
                        onEditClick = { showEditConfirmation.value = true }
                    )
                    }
                }
            }
        }
    if (showEditConfirmation.value) {
        ShowEditConfirm(
            showEditConfirmation = showEditConfirmation,
            onConfirm = {
                val intent = Intent(currentContext, QuestionnaireActivity::class.java)
                currentContext.startActivity(intent)
                onEditClick()
            },
        )
    }
}



@Composable
fun UserProfilePage(modifier: Modifier = Modifier) {
    val showLogoutConfirmation = remember { mutableStateOf(false) } // Show Dialog for Logout
    val showEditConfirmation = remember { mutableStateOf(false) } // Show Dialog for Editing Preferences
    val currentContext = LocalContext.current
    val view = AuthViewModel(currentContext)
    val userView = UserProfileViewModel(currentContext)
    var username by remember { mutableStateOf("") }
    var fetchedUsername by remember { mutableStateOf("") }
    var fetchedEmail by remember { mutableStateOf("") }
    var fetchedAnswers by remember { mutableStateOf<Map<String, Set<String>>?>(null) }
    var isPreferencesExpanded by remember { mutableStateOf(false) } // used to toggle up/down arrow for preferences

    LaunchedEffect(Unit) {
        val fetchedUserInfo = userView.getProfileInfo()

        fetchedUsername = fetchedUserInfo.username
        fetchedEmail = fetchedUserInfo.email
        fetchedAnswers = fetchedUserInfo.answers
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(16.dp))
        UserProfileCard(
            modifier = Modifier.weight(1f),
            fetchedUsername = fetchedUsername,
            fetchedEmail = fetchedEmail,
            fetchedAnswers = fetchedAnswers,
            onUsernameChange = { username = it },
            onSaveClick = { userView.updateUsername(username) },
            isPreferencesExpanded = isPreferencesExpanded,
            onPreferencesToggle = { isPreferencesExpanded = !isPreferencesExpanded },
            onEditClick = { showEditConfirmation.value = true },
            currentContext = currentContext
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            LogoutButton(
                onClick = { showLogoutConfirmation.value = true },
                modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp)
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
