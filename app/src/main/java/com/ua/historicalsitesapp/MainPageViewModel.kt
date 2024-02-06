package com.ua.historicalsitesapp

import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.model.GetBearerTokens
import com.ua.historicalsitesapp.data.model.Location
import com.ua.historicalsitesapp.data.model.LoggedInUser
import com.ua.historicalsitesapp.data.model.UserFavoriteLocationDeleteRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

class MainPageViewModel() : ViewModel() {
    private val loginRepository = LoginRepositoryProvider.provideLoginRepository(LoginDataSource())

    fun getUserClient(): HttpClient {
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
                            urlString = "http://10.0.2.2:8080/api/refresh"
                        ) { markAsRefreshTokenRequest() }.body()
                        usertokens = GetBearerTokens(refreshTokenInfo)
                        usertokens
                    }
                }
            }
        }
        return client
    }

    private fun getUser(): LoggedInUser {
        return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
    }

    fun deleteUserFavoriteLocation(location: UserFavoriteLocationDeleteRequest) {
        val client = getUserClient()
        runBlocking {
            val response = client.delete("http://10.0.2.2:8080/api/user/favorite_locations") {
                contentType(ContentType.Application.Json)
                setBody(location)
            }
        }
    }

    fun getUsersFavoriteLocations(): List<Location> {
        val client = getUserClient()
        return runBlocking {
            val response: List<Location> =
                client.get("http://10.0.2.2:8080/api/user/favorite_locations") {

                }.body()
            return@runBlocking response
        }

    }

}
