package com.ua.historicalsitesapp.data.model.map

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HsLocation(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    @SerialName("short_description") val shortDescription: String?,
    @SerialName("wikidata_image_name") val wikidataImageName: String,
)

@Serializable
data class HsLocationComplete(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    @SerialName("short_description") val shortDescription: String?,
    @SerialName("long_description") val longDescription: String?,
    @SerialName("image_link") val imageLink: String,
)
