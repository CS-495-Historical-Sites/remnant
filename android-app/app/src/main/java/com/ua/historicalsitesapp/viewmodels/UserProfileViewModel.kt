package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.GetBearerTokens
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.util.ServerConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json

class UserProfileViewModel(context: Context) : ViewModel() {
  private val loginRepository =
      LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

  private fun getUser(): LoggedInUser {
    return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
  }

  fun getUserClient(): HttpClient {
    val user = getUser()
    var usertokens = GetBearerTokens(user)
    val client =
        HttpClient(CIO) {
          install(ContentNegotiation) { json() }

          install(Auth) {
            bearer {
              loadTokens { usertokens }
              refreshTokens {
                val refreshTokenInfo: LoggedInUser =
                    client
                        .post(
                            urlString = ServerConfig.SERVER_URL + "/refresh",
                        ) {
                          markAsRefreshTokenRequest()
                        }
                        .body()
                usertokens = GetBearerTokens(refreshTokenInfo)
                usertokens
              }
            }
          }
        }
    return client
  }

  fun submitRegistrationQuestionare(answers: Map<String, Set<String>>) {}
}
