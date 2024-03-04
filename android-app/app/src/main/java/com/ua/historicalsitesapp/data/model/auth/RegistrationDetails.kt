package com.ua.historicalsitesapp.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationDetails(
    val email: String,
    val password: String,
)
