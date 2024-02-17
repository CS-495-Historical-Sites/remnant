package com.ua.historicalsitesapp.authUI

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ua.historicalsitesapp.MainPageActivity
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

enum class PasswordValidationResult {
    OK,
    InvalidChars,
    InvalidLength
}

private fun isEmailValid(email: String): Boolean {
    return Regex("[\\w-]+@([\\w-]+\\.)+[\\w-]+").matches(email)
}

private fun validatePassword(password: String): PasswordValidationResult {
    val regex = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%*?&]+$""".toRegex()
    if (!regex.matches(password)) return PasswordValidationResult.InvalidChars
    return if (password.length !in 8..24) PasswordValidationResult.InvalidLength else PasswordValidationResult.OK
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
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        },
        label = { Text("Email") },
        maxLines = 1,
        singleLine = true,
        isError = isError,
        keyboardActions = KeyboardActions { validate(text) },
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
        label = { Text("Password") },
        // only display length text if passing char regex
        supportingText = {
            if (!isPassingCharRegex) {
                Text(
                    text = "Password requires at least 1 special character and digit",
                    textAlign = TextAlign.Left,
                    style = TextStyle(fontSize = 14.sp)
                )
            } else if (!isLengthReq) {
                Text(
                    text = "Password must be 8-24 characters long.",
                    textAlign = TextAlign.Left,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        },
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
        }
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
private fun RegistrationCard(
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
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

            Box(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                EmailTextField(onEmailChange = onEmailChange)
            }

            Box(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                PasswordTextField(onPasswordChange = onPasswordChange)
            }

            RegisterButton(onRegisterClick)
            LoginButton(onLoginClick)
        }
    }
}

@Composable
private fun RegistrationMenu(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val registrationView = RegistrationViewModel(context)

    if (registrationView.isLoggedIn()) {
        val intent = Intent(context, MainPageActivity::class.java)
        context.startActivity(intent)
    }



    RegistrationCard(
        onEmailChange = { registrationView.email = it },
        onPasswordChange = { registrationView.password = it },
        onRegisterClick = {
            if (!isEmailValid(registrationView.email)) {
                return@RegistrationCard
            }

            if (validatePassword(registrationView.password) != PasswordValidationResult.OK) {
                return@RegistrationCard
            }


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



