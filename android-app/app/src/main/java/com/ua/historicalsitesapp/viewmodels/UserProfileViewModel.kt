package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.util.constructUserClient
import io.ktor.client.HttpClient

class UserProfileViewModel(context: Context) : ViewModel() {
  private val loginRepository =
      LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

  private fun getUser(): LoggedInUser {
    return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
  }

  fun getUserClient(): HttpClient {
    val user = getUser()
    return constructUserClient(user)
  }

  fun submitRegistrationQuestionare(answers: Map<String, Set<String>>) {}
}
