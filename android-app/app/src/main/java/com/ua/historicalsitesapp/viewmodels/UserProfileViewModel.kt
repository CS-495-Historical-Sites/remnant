package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.model.userinfo.UserProfileUpdateRequest
import com.ua.historicalsitesapp.util.constructUserClient
import io.ktor.client.HttpClient
import com.ua.historicalsitesapp.util.ServerConfig
import io.ktor.client.request.post
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

  fun getUserClient(): HttpClient {
    val user = getUser()
    return constructUserClient(user)
  }


  fun submitRegistrationQuestionnaire(answers: Map<String, Set<String>>): Boolean {
    val client = getUserClient()
    val allAnswers = UserProfileUpdateRequest(answers["Preferred Historical Period:"])
    return runBlocking {
      val response =
          client.post(ServerConfig.SERVER_URL + "api/user/privateinfo") {
            contentType(ContentType.Application.Json)
            setBody(allAnswers)
          }
      return@runBlocking response.status.value == 200
    }
  }
}