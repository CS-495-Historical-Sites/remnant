package com.ua.historicalsitesapp.data.repository.locations

import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.util.ServerConfig
import com.ua.historicalsitesapp.util.constructUserClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

class LocationDataSource {

  fun getLocationsNearPoint(
      user: LoggedInUser,
      point: LatLng,
      kilometerRadius: Float
  ): List<HsLocation> {
    val client = constructUserClient(user)

    val latitude = point.latitude
    val longitude = point.longitude

    return runBlocking {
      val response: List<HsLocation> =
          client
              .get(ServerConfig.SERVER_URL + "/locations") {
                parameter("lat", latitude)
                parameter("long", longitude)
                parameter("kilometer_radius", kilometerRadius)
              }
              .body()
      return@runBlocking response
    }
  }

  fun getLocationInfo(user: LoggedInUser, locationId: Int): HsLocationComplete {
    val client = constructUserClient(user)

    return runBlocking {
      val response: HsLocationComplete =
          client.get(ServerConfig.SERVER_URL + "/locations/" + locationId) {}.body()
      return@runBlocking response
    }
  }
}
