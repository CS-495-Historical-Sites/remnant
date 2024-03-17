package com.ua.historicalsitesapp.util

import com.ua.historicalsitesapp.data.model.auth.GetBearerTokens
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json

fun constructUserClient(user: LoggedInUser): HttpClient {
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
