package com.ua.historicalsitesapp.util

import com.ua.historicalsitesapp.data.model.auth.GetBearerTokens
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.viewmodels.RemnantUnauthorizedAccessException
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import kotlinx.serialization.json.Json

fun constructUserClient(user: LoggedInUser): HttpClient {
  var usertokens = GetBearerTokens(user)
  val client =
      HttpClient(CIO) {
        install(ContentNegotiation) { Json { ignoreUnknownKeys = true } }

        install(Auth) {
          bearer {
            loadTokens { usertokens }

            refreshTokens {
              try {
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
              } catch (e: NoTransformationFoundException) {
                throw RemnantUnauthorizedAccessException("Couldn't refresh tokens")
              }
            }
          }
        }
      }
  return client
}
