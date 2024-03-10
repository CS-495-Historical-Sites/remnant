package com.ua.historicalsitesapp.data.repository.locations

import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.util.ServerConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class LocationDataSource {
  private fun getClient(): HttpClient {
    return HttpClient(CIO) {
      install(ContentNegotiation) {
        json(
            Json {
              prettyPrint = true
              isLenient = true
            },
        )
      }
      install(HttpCache)
    }
  }

  fun getAllLocations(): List<HsLocation> {
    val client = getClient()

    return runBlocking {
      val response: List<HsLocation> = client.get(ServerConfig.SERVER_URL + "/locations") {}.body()
      return@runBlocking response
    }
  }

  fun getLocationsNearPoint(point: LatLng, kilometerRadius: Float): List<HsLocation> {
    val client = getClient()

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

  fun getLocationInfo(locationId: Int): HsLocationComplete {
    val client = getClient()

    return runBlocking {
      val response: HsLocationComplete =
          client.get(ServerConfig.SERVER_URL + "/locations/" + locationId) {}.body()
      return@runBlocking response
    }
  }
}
