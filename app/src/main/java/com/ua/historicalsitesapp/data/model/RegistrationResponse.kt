package com.ua.historicalsitesapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponse(
    val email: String?,
    val errorString: String?
)

enum class RegistrationResult {
    SUCCESS, FAILURE
}
