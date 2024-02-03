package com.ua.historicalsitesapp.data

import com.ua.historicalsitesapp.data.model.LoggedInUser
import com.ua.historicalsitesapp.data.model.LoginDetails
import com.ua.historicalsitesapp.data.model.RegistrationDetails
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private suspend fun sendLoginRequest(details: LoginDetails) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("http://localhost:8080/") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
    }

    private suspend fun sendRegistrationRequest(details: RegistrationDetails) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("http://localhost:8080/api/register") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
    }

    suspend fun login(userDetails: LoginDetails): Result<LoggedInUser> {
        try {
            val result = sendLoginRequest(userDetails)

            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun register(userDetails: RegistrationDetails): Result<LoggedInUser> {
        try {
            val result = sendRegistrationRequest(userDetails)
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
