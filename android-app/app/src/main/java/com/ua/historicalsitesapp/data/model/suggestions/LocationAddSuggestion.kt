package com.ua.historicalsitesapp.data.model.suggestions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.util.Base64


@Serializable
data class LocationAddSuggestion(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("image") val image: String,
    @SerialName("wikipedia_link") val wikipediaLink: String?
)
