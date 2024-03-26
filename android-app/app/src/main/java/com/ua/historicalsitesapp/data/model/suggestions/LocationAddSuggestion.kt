package com.ua.historicalsitesapp.data.model.suggestions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("ArrayInDataClass")
@Serializable
data class LocationAddSuggestion(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("image") val image: ByteArray,
    @SerialName("wikipedia_link") val wikipediaLink: String?
)
