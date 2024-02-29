package com.ua.historicalsitesapp.data.model.visits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserVisitedLocationsResponse(
    @SerialName("visited_locations") val visitedLocations: List<UserVisit>
)


@Serializable
data class UserVisit(
    val id: Int,
    val name: String
)
