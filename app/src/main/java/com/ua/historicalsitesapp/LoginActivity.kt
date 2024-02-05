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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ua.historicalsitesapp.data.Result
import com.ua.historicalsitesapp.ui.theme.HistoricalSitesAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoricalSitesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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
fun LoginCard(
    modifier: Modifier = Modifier,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Card() {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign in", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            UsernameTextField(onUsernameChange = onUsernameChange)
            PasswordTextField(onPasswordChange = onPasswordChange)

            LoginButton(onLoginClick)

            RegisterButton(onRegisterClick)

        }
    }
}

@Composable
fun LoginMenu(modifier: Modifier = Modifier) {
    val loginView = LoginViewModel()
    val context = LocalContext.current
    LoginCard(
        onUsernameChange = { loginView.username = it },
        onPasswordChange = { loginView.password = it },
        onLoginClick = {
            val loginResult = loginView.performLogin()

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

