package com.ua.historicalsitesapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginDetails(
    val email: String,
    val password: String
)