package com.ua.historicalsitesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ua.historicalsitesapp.ui.theme.HistoricalSitesAppTheme

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationMenu()
                }
            }
        }
    }
}

@Composable
fun UsernameTextField(onUsernameChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onUsernameChange(it)
        },
        label = { Text("Username") },
        maxLines = 1
    )
}

@Composable
fun PasswordTextField(onPasswordChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onPasswordChange(it)
        },
        label = { Text("Password") },
        maxLines = 1
    )
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Register")
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() }) {
        Text("Login")
    }
}

@Composable
fun RegistrationCard(
    modifier: Modifier = Modifier,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    Card() {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UsernameTextField(onUsernameChange = onUsernameChange)
            PasswordTextField(onPasswordChange = onPasswordChange)
            RegisterButton(onRegisterClick)
            LoginButton({})
        }
    }
}

@Composable
fun RegistrationMenu(modifier: Modifier = Modifier) {
    val registrationView = RegistrationViewModel()

    RegistrationCard(
        onUsernameChange = { registrationView.username = it },
        onPasswordChange = { registrationView.password = it },
        onRegisterClick = {
            registrationView.performLogin()
        }
    )

}

