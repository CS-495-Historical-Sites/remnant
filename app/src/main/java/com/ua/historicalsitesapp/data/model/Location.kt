package com.ua.historicalsitesapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HsLocation(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float
)
