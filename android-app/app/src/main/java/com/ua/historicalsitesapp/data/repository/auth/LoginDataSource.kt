package com.ua.historicalsitesapp.data.repository.auth

import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.auth.LoginDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationResponse
import com.ua.historicalsitesapp.data.model.auth.RegistrationResult
import com.ua.historicalsitesapp.util.Result
import com.ua.historicalsitesapp.util.ServerConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.IOException

/** Class that handles authentication w/ login credentials and retrieves user information. */
class LoginDataSource {
  private suspend fun sendLoginRequest(details: LoginDetails): LoggedInUser {
    val client =
        HttpClient(CIO) {
          install(ContentNegotiation) {
            json(
                Json {
                  prettyPrint = true
                  isLenient = true
                },
            )
          }
        }
    val response: HttpResponse =
        client.post(
            ServerConfig.SERVER_URL + "/user/login",
        ) {
          contentType(ContentType.Application.Json)
          setBody(details)
        }
    if (response.status.value != 200) {
      throw java.lang.Exception("login request error")
    }

    return response.body()
  }

  private suspend fun sendRegistrationRequest(regDetails: RegistrationDetails): RegistrationResult {
    val client =
        HttpClient(CIO) {
          install(ContentNegotiation) {
            json(
                Json {
                  prettyPrint = true
                  isLenient = true
                },
            )
          }
        }
    try {
      val response: HttpResponse =
          client.post(ServerConfig.SERVER_URL + "/user/register") {
            contentType(ContentType.Application.Json)
            setBody(regDetails)
          }
      if (response.status.value == 200) {
        val userDetails: RegistrationResponse = response.body()
        return RegistrationResult.SUCCESS
      } else if (response.status.value == 422) {
        return RegistrationResult.DUPLICATE
      }
    } catch (e: Exception) {
      println("Request failed with exception: $e")
    }
    return RegistrationResult.FAILURE
  }

  suspend fun login(userDetails: LoginDetails): Result<LoggedInUser> {
    try {
      val user = sendLoginRequest(userDetails)
      return Result.Success(user)
    } catch (e: Throwable) {
      return Result.Error(IOException("Error logging in", e))
    }
  }

  suspend fun register(userDetails: RegistrationDetails): RegistrationResult {
    return sendRegistrationRequest(userDetails)
  }

  suspend fun logout(tokenObject: LoggedInUser): LogoutResult {
    return sendLogoutRequest(tokenObject)
  }

  sealed class LogoutResult {
    data object Success : LogoutResult()

    data class Failure(val error: Throwable) : LogoutResult()
  }

  private suspend fun sendLogoutRequest(tokenObject: LoggedInUser): LogoutResult {
    try {
      val client =
          HttpClient(CIO) {
            install(ContentNegotiation) {
              json(
                  Json {
                    prettyPrint = true
                    isLenient = true
                  },
              )
            }
          }

      val response: HttpResponse =
          client.delete(ServerConfig.SERVER_URL + "/user/logout") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${tokenObject.accessToken}") }
          }
      if (response.status.value != 200) {
        throw java.lang.Exception("logout request error")
      }

      val refreshResponse: HttpResponse =
          client.delete(ServerConfig.SERVER_URL + "/user/logout") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${tokenObject.refreshToken}") }
          }
      if (refreshResponse.status.value != 200) {
        throw java.lang.Exception("logout request error")
      }

      return LogoutResult.Success
    } catch (e: Exception) {
      println("Exception during logout: ${e.message}")
      return LogoutResult.Failure(e)
    }
  }
}
