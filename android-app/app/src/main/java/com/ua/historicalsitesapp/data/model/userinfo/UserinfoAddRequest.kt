package com.ua.historicalsitesapp.data.model.userinfo

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUpdateRequest( val answers: Map<String, Set<String>>?)

@Serializable
data class UserProfileUsernameRequest( val username: String)

@Serializable
data class UserProfileInfo(
    val username: String,
    val answers: Map<String, Set<String>>?,
    val email: String
)