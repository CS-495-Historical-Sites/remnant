package com.ua.historicalsitesapp.data.model

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Serializable
data class LoggedInUser(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

fun GetBearerTokens(user: LoggedInUser): BearerTokens {
    return BearerTokens(user.accessToken, user.refreshToken)
}