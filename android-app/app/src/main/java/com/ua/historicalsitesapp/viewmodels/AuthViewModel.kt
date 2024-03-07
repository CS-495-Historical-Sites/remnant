package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.auth.LoginDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationResult
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.util.Result
import kotlinx.coroutines.runBlocking

class AuthViewModel(context: Context) : ViewModel() {
  private val loginRepository =
      LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

  fun performRegistration(
      username: String,
      email: String,
      password: String,
  ): RegistrationResult? {
    var result: RegistrationResult? = null
    runBlocking { result = loginRepository.register(RegistrationDetails(username, email, password)) }

    return result
  }

  fun isLoggedIn(): Boolean {
    return loginRepository.isLoggedIn
  }

  fun performLogin(
      email: String,
      password: String,
  ): Result<LoggedInUser> {
    var result: Result<LoggedInUser> = Result.Error(Exception(("Unknown Error")))
    runBlocking { result = loginRepository.login(LoginDetails(email, password)) }

    return result
  }

  fun performLogout(): Boolean {
    val logoutResult: Boolean
    if (loginRepository.isLoggedIn) {
      logoutResult = loginRepository.logout()
      return logoutResult
    }
    return false
  }
}
