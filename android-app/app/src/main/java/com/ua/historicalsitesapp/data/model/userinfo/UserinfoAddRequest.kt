package com.ua.historicalsitesapp.data.model.userinfo
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUpdateRequest(
    var interested_eras: Set<String>?
)
