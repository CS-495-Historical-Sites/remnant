package com.ua.historicalsitesapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Int,
    val name: String
)

@Serializable
data class UserFavoriteLocationDeleteRequest(
    @SerialName("location_id") val id: Int,
)

