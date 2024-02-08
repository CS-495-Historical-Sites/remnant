package com.ua.historicalsitesapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ua.historicalsitesapp.data.model.RegistrationResult
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
private fun RegisterButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Register")
    }
}

@Composable
private fun LoginButton(onClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() }) {
        Text("Login")
    }
}

@Composable
fun RegistrationCard(
    modifier: Modifier = Modifier,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Card {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign up", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            UsernameTextField(onUsernameChange = onUsernameChange)
            PasswordTextField(onPasswordChange = onPasswordChange)
            RegisterButton(onRegisterClick)
            LoginButton(onLoginClick)
        }
    }
}

@Composable
fun RegistrationMenu(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val registrationView = RegistrationViewModel(context)

    if (registrationView.isLoggedIn()) {
        val intent = Intent(context, MainPageActivity::class.java)
        context.startActivity(intent)
    }


    RegistrationCard(
        onUsernameChange = { registrationView.username = it },
        onPasswordChange = { registrationView.password = it },
        onRegisterClick = {
            val registrationResult = registrationView.performRegistration()
            if (registrationResult == RegistrationResult.SUCCESS) {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        },
        onLoginClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    )

}



