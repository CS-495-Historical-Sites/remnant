package com.ua.historicalsitesapp.data

import com.ua.historicalsitesapp.data.model.LoggedInUser
import com.ua.historicalsitesapp.data.model.LoginDetails
import com.ua.historicalsitesapp.data.model.RegistrationDetails
import com.ua.historicalsitesapp.data.model.RegistrationResponse
import com.ua.historicalsitesapp.data.model.RegistrationResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private suspend fun sendLoginRequest(details: LoginDetails): LoggedInUser {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val response: HttpResponse = client.post("http://10.0.2.2:8080/api/login") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
        if (response.status.value != 200) {
            throw java.lang.Exception("login request error")
        }

        return response.body()
    }

    private suspend fun sendRegistrationRequest(regDetails: RegistrationDetails): RegistrationResult {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        try {
            val response: HttpResponse = client.post("http://10.0.2.2:8080/api/register") {
                contentType(ContentType.Application.Json)
                setBody(regDetails)
            }
            if (response.status.value == 200) {
                val userDetails: RegistrationResponse = response.body()
                return RegistrationResult.SUCCESS
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

    fun logout() {
        // TODO: revoke authentication
    }
}
