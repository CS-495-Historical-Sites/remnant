package com.ua.historicalsitesapp.data.model.suggestions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationEditSuggestion(
    val name: String,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("long_description") val longDescription: String,
)
