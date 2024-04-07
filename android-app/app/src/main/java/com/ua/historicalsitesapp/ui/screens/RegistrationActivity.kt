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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ua.historicalsitesapp.data.model.auth.RegistrationResult
import com.ua.historicalsitesapp.ui.theme.Typography
import com.ua.historicalsitesapp.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

class RegistrationActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HistoricalSitesAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          RegistrationMenu()
        }
      }
    }
  }
}

enum class PasswordValidationResult {
  OK,
  InvalidChars,
  InvalidLength,
}

@Composable
private fun UsernameTextField(onUserNameChange: (String) -> Unit) {
  var text by remember { mutableStateOf("") }

  OutlinedTextField(
      value = text,
      onValueChange = {
        text = it
        onUserNameChange(it)
      },
      label = { Text("User Name") },
      maxLines = 1,
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default,
      shape = RoundedCornerShape(12.dp),
      colors =
          OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.secondary))
}

private fun isEmailValid(email: String): Boolean {
  return Regex("[\\w.-]+@([\\w-]+\\.)+[\\w-]+").matches(email)
}

private fun validatePassword(password: String): PasswordValidationResult {
  // should be fixed let Clayton know if its not fixed
  val regex = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]+$""".toRegex()
  if (!regex.matches(password)) return PasswordValidationResult.InvalidChars
  return if (password.length !in 8..24) PasswordValidationResult.InvalidLength
  else PasswordValidationResult.OK
}

@Composable
private fun EmailTextField(onEmailChange: (String) -> Unit) {
  var isError by rememberSaveable { mutableStateOf(false) }

  fun validate(text: String) {
    isError = !isEmailValid(text)
  }

  var text by remember { mutableStateOf("") }
  OutlinedTextField(
      value = text,
      onValueChange = {
        text = it
        onEmailChange(it)
        validate(it)
      },
      supportingText = {
        if (isError) {
          Text(
              text = "Please enter a valid email",
              textAlign = TextAlign.Left,
              style = TextStyle(fontSize = 14.sp),
          )
        }
      },
      label = { Text("Your email") },
      maxLines = 1,
      singleLine = true,
      isError = isError,
      keyboardActions = KeyboardActions { validate(text) },
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
  var isPassingCharRegex by rememberSaveable { mutableStateOf(true) }
  var isLengthReq by rememberSaveable { mutableStateOf(true) }

  fun validate(text: String) {
    when (validatePassword(text)) {
      PasswordValidationResult.OK -> {
        isPassingCharRegex = true
        isLengthReq = true
      }
      PasswordValidationResult.InvalidChars -> isPassingCharRegex = false
      PasswordValidationResult.InvalidLength -> {
        isPassingCharRegex = true
        isLengthReq = false
      }
    }
  }

  OutlinedTextField(
      value = text,
      onValueChange = {
        text = it
        onPasswordChange(it)
        validate(it)
      },
      label = {
        Text(
            "Enter your password",
        )
      },
      // only display length text if passing char regex
      supportingText = {
        if (!isPassingCharRegex) {
          Text(
              text = "Password requires at least 1 special character and digit",
              textAlign = TextAlign.Left,
              style = TextStyle(fontSize = 14.sp),
          )
        } else if (!isLengthReq) {
          Text(
              text = "Password must be 8-24 characters long.",
              textAlign = TextAlign.Left,
              style = TextStyle(fontSize = 14.sp),
          )
        }
      },
      maxLines = 1,
      singleLine = true,
      isError = !(isPassingCharRegex and isLengthReq),
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
private fun RegisterButton(
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
        "Create account",
        style = Typography.labelLarge,
    )
  }
}

@Composable
fun RegistrationCard(
    modifier: Modifier = Modifier,
    onUsernameChange: (String) -> Unit,
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
            "Create account",
            style =
                TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp, // Adjust the font size as needed
                    textAlign = TextAlign.Start, // Align the text to the start (left)
                ),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column( // First and Last name
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
              Text("User Name", modifier = Modifier.padding(start = 8.dp))
              UsernameTextField(onUserNameChange = onUsernameChange)

              Column(
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.Start,
              ) {
                Text("Email", modifier = Modifier.padding(start = 8.dp))
                EmailTextField(onEmailChange = onEmailChange)
              }
              Column(
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.Start,
              ) {
                Text("Create a password", modifier = Modifier.padding(start = 8.dp))
                PasswordTextField(onPasswordChange = onPasswordChange)
              }

              RegisterButton(onRegisterClick, modifier = Modifier.fillMaxWidth(0.95f))
              Spacer(modifier = Modifier.height(50.dp))
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Text("Already have an account? ", style = MaterialTheme.typography.labelLarge)
                Text(
                    "Log in",
                    style =
                        MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.clickable { onLoginClick() },
                )
              }
            }
      }
    }
  }
}

@Composable
private fun RegistrationMenu(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val registrationView = AuthViewModel(context)

  if (registrationView.isLoggedIn()) {
    val intent = Intent(context, MainPageActivity::class.java)
    context.startActivity(intent)
  }
  var username by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var registerSuccess by remember { mutableStateOf(false) }
  var duplicateError by remember { mutableStateOf(false) }
  RegistrationCard(
      onUsernameChange = { username = it },
      onEmailChange = { email = it },
      onPasswordChange = { password = it },
      onRegisterClick = {
        if (!isEmailValid(email)) {
          return@RegistrationCard
        }

        if (validatePassword(password) != PasswordValidationResult.OK) {
          return@RegistrationCard
        }

        val registrationResult = registrationView.performRegistration(username, email, password)
        if (registrationResult == RegistrationResult.SUCCESS) {
          registerSuccess = true
        } else if (registrationResult == RegistrationResult.DUPLICATE) {
          duplicateError = true
        }
      },
      onLoginClick = {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
      },
  )
  if (duplicateError) {
    AlertDialog(
        onDismissRequest = { duplicateError = false },
        title = {
          Text(
              text = "We already have an account associated with this email!",
              textAlign = TextAlign.Left,
              modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        },
        text = {
          Text(
              text = "Would you like to log in?",
              textAlign = TextAlign.Center,
          )
        },
        confirmButton = {
          Row(
              modifier = Modifier.fillMaxWidth().padding(8.dp),
              horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { duplicateError = false },
                    colors =
                        ButtonDefaults.buttonColors(
                            contentColor = Color.White, containerColor = Color.Black),
                    modifier = Modifier.width(115.dp)) {
                      Text("Cancel")
                    }
                Button(
                    onClick = {
                      duplicateError = false
                      val intent = Intent(context, LoginActivity::class.java)
                      context.startActivity(intent)
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            contentColor = Color.White, containerColor = Color.Black),
                    modifier = Modifier.width(115.dp)) {
                      Text("Log In")
                    }
              }
        },
    )
  }
  if (registerSuccess) {
    registerSnackbar("Successfully Registered!")
    LaunchedEffect(Unit) {
      delay(1000)
      registerSuccess = false
      val intent = Intent(context, LoginActivity::class.java)
      context.startActivity(intent)
    }
  }
}

@Composable
fun registerSnackbar(message: String) {
  val snackbarHostState = SnackbarHostState()

  LaunchedEffect(snackbarHostState) {
    snackbarHostState.showSnackbar(message = message)
    snackbarHostState.currentSnackbarData?.dismiss()
  }

  SnackbarHost(
      hostState = snackbarHostState,
      snackbar = { Snackbar(modifier = Modifier.padding(16.dp), action = {}) { Text(message) } })
}
