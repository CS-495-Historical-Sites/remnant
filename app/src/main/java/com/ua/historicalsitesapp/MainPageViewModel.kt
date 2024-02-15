package com.ua.historicalsitesapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.model.GetBearerTokens
import com.ua.historicalsitesapp.data.model.HsLocation
import com.ua.historicalsitesapp.data.model.LoggedInUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

class MainPageViewModel(context: Context) : ViewModel() {

    private val loginRepository =
        LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

    private fun getUserClient(): HttpClient {
        val user = getUser()
        var usertokens = GetBearerTokens(user)
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        usertokens
                    }
                    refreshTokens {
                        val refreshTokenInfo: LoggedInUser = client.post(
                            urlString = ServerConfig.SERVER_URL + "/refresh"
                        ) { markAsRefreshTokenRequest() }.body()
                        usertokens = GetBearerTokens(refreshTokenInfo)
                        usertokens
                    }
                }
            }
        }
        return client
    }

    fun getAllLocations(): List<HsLocation> {
        val client = getUserClient()
        return runBlocking {
            val response: List<HsLocation> =
                client.get(ServerConfig.SERVER_URL + "/locations") {
                }.body()
            return@runBlocking response
        }

    }

    private fun getUser(): LoggedInUser {
        return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
    }
}
