package com.ua.historicalsitesapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.HistoricalSitesAppTheme
import com.ua.historicalsitesapp.ui.theme.Typography
import com.ua.historicalsitesapp.util.Result
import com.ua.historicalsitesapp.viewmodels.AuthViewModel

class LoginActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HistoricalSitesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
          LoginMenu()
        }
      }
    }
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
      label = { Text("Your email") },
      maxLines = 1,
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      colors =
          OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.secondary,
          ),
  )
}

@Composable
private fun PasswordTextField(onPasswordChange: (String) -> Unit) {
  var text by remember { mutableStateOf("") }
  var passwordHidden by rememberSaveable { mutableStateOf(true) }

  OutlinedTextField(
      value = text,
      onValueChange = {
        text = it
        onPasswordChange(it)
      },
      label = {
        Text(
            "Enter your password",
        )
      },
      maxLines = 1,
      singleLine = true,
      visualTransformation =
          if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      trailingIcon = {
        IconButton(onClick = { passwordHidden = !passwordHidden }) {
          val visibilityIcon =
              if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
          // Please provide localized description for accessibility services
          val description = if (passwordHidden) "Show password" else "Hide password"
          Icon(imageVector = visibilityIcon, contentDescription = description)
        }
      },
      shape = RoundedCornerShape(12.dp),
      colors =
          OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.secondary,
          ),
  )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
  Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
      shape = RoundedCornerShape(12.dp),
      modifier = modifier.fillMaxWidth().height(56.dp), // Adjust the height here
  ) {
    Text(
        "Log in ",
        style = Typography.labelLarge,
    )
  }
}

@Composable
fun LoginCard(
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
  Box(
      modifier = Modifier.fillMaxSize(),
  ) {
    Box(
        modifier =
            Modifier.size(width = 300.dp, height = 800.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .align(Alignment.TopCenter), // Align to the top of the parent
    ) {
      Column(
          modifier = modifier.fillMaxSize(),
          horizontalAlignment = Alignment.Start, // Align to the start (left) of the parent
          verticalArrangement = Arrangement.Center,
      ) {
        Text(
            "Log in",
            style =
                TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp, // Adjust the font size as needed
                    textAlign = TextAlign.Start, // Align the text to the start (left)
                ),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
          Text("Email", modifier = Modifier.padding(start = 8.dp))
          EmailTextField(onEmailChange = onEmailChange)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
          Text("Password", modifier = Modifier.padding(start = 8.dp))
          PasswordTextField(onPasswordChange = onPasswordChange)
        }
        Spacer(modifier = Modifier.height(20.dp))
        LoginButton(onLoginClick, modifier = Modifier.fillMaxWidth(0.95f))
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text("Don't have an account? ", style = MaterialTheme.typography.labelLarge)
          Text(
              "Sign up",
              style =
                  MaterialTheme.typography.labelLarge.copy(
                      color = MaterialTheme.colorScheme.primary),
              modifier = Modifier.clickable { onRegisterClick() },
          )
        }
      }
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
          if (loginResult.data.isFirstLogin) {
            val intent = Intent(context, QuestionnaireActivity::class.java)
            context.startActivity(intent)
          } else {
            val intent = Intent(context, MainPageActivity::class.java)
            context.startActivity(intent)
          }
        }
      },
      onRegisterClick = {
        val intent = Intent(context, RegistrationActivity::class.java)
        context.startActivity(intent)
      },
  )
}
