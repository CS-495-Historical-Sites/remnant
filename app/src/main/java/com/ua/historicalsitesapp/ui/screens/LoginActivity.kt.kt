package com.ua.historicalsitesapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ua.historicalsitesapp.ui.theme.HistoricalSitesAppTheme
import com.ua.historicalsitesapp.util.Result
import com.ua.historicalsitesapp.viewmodels.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                Surface(
//                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginMenu()
                }
            }
        }
    }
}

@Composable
private fun RegisterButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text("Register")
    }
}

@Composable
private fun LoginButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Login")
    }
}

@Composable
private fun EmailTextField(onEmailChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onEmailChange(it)
        },
        label = { Text("Email") },
        maxLines = 1
    )
}

@Composable
private fun PasswordTextField(onPasswordChange: (String) -> Unit) {
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
private fun LoginCard(
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Card(modifier = Modifier.size(300.dp)) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign in", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            EmailTextField(onEmailChange = onEmailChange)
            PasswordTextField(onPasswordChange = onPasswordChange)

            LoginButton(onLoginClick)

            RegisterButton(onRegisterClick)

        }
    }
}

@Composable
private fun LoginMenu(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val view = AuthViewModel(context)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LoginCard(
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            val loginResult = view.performLogin(email, password)

            if (loginResult is Result.Success) {
                val intent = Intent(context, MainPageActivity::class.java)
                context.startActivity(intent)
            }
        },
        onRegisterClick = {
            val intent = Intent(context, RegistrationActivity::class.java)
            context.startActivity(intent)
        }
    )

}

