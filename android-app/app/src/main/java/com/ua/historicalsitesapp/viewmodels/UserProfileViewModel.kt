package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.userinfo.UserProfileInfo
import com.ua.historicalsitesapp.data.model.userinfo.UserProfileUpdateRequest
import com.ua.historicalsitesapp.data.model.userinfo.UserProfileUsernameRequest
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.util.ServerConfig
import com.ua.historicalsitesapp.util.constructUserClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class UserProfileViewModel(context: Context) : ViewModel() {
  private val loginRepository =
      LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

  private fun getUser(): LoggedInUser {
    return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
  }

  private fun getUserClient(): HttpClient {
    val user = getUser()
    return constructUserClient(user)
  }

  fun submitRegistrationQuestionnaire(answers: Map<String, Set<String>>): Boolean {
    val client = getUserClient()
    val allAnswers = UserProfileUpdateRequest(answers)
    return runBlocking {
      val response =
          client.patch(ServerConfig.SERVER_URL + "/user/privateinfo") {
            contentType(ContentType.Application.Json)
            setBody(allAnswers)
          }
      return@runBlocking response.status.value == 200
    }
  }

  fun getUsername(username: String): UserProfileInfo {
    val client = getUserClient()
    return runBlocking {
      val response: UserProfileInfo =
          client.get(ServerConfig.SERVER_URL + "/user/privateinfo" + username) {}.body()
      return@runBlocking response
    }
  }

  fun getEmail(email: String): UserProfileInfo {
    val client = getUserClient()
    return runBlocking {
      val response: UserProfileInfo =
          client.get(ServerConfig.SERVER_URL + "/user/privateinfo" + email) {}.body()
      return@runBlocking response
    }
  }

  fun updateUsername(username: String): Boolean {
    val client = getUserClient()
    val newUsername = UserProfileUsernameRequest(username)
    return runBlocking {
      val response =
          client.patch(ServerConfig.SERVER_URL + "/user/privateinfo") {
            contentType(ContentType.Application.Json)
            setBody(newUsername)
          }
      return@runBlocking response.status.value == 200
    }
  }
}
