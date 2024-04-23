package com.ua.historicalsitesapp.data.model.map

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HsLocation(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    @SerialName("is_liked") val isLiked: Boolean,
    @SerialName("short_description") val shortDescription: String?,
    @SerialName("image_link") val imageLink: String,
    @SerialName("categories") val associatedCategories: List<String>
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
