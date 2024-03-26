package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.data.model.suggestions.LocationAddSuggestion
import com.ua.historicalsitesapp.data.model.suggestions.LocationEditSuggestion
import com.ua.historicalsitesapp.data.model.visits.GetUserVisitedLocationsResponse
import com.ua.historicalsitesapp.data.model.visits.VisitAddRequest
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.repository.locations.LocationDataSource
import com.ua.historicalsitesapp.data.repository.locations.LocationRepository
import com.ua.historicalsitesapp.util.ServerConfig
import com.ua.historicalsitesapp.util.constructUserClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class MainPageViewModel(context: Context) : ViewModel() {
  private val locationRepository = LocationRepository(LocationDataSource())

  private val loginRepository =
      LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

  private fun getUser(): LoggedInUser {
    return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
  }

  private fun getUserClient(): HttpClient {
    val user = getUser()
    return constructUserClient(user)
  }

  fun getHistoricalLocationNearPoint(point: LatLng, kilometerRadius: Float): List<HsLocation> {
    val user = getUser()
    return locationRepository.getLocationsNearPoint(user, point, kilometerRadius)
  }

  fun getLocationInfo(locationId: Int): HsLocationComplete {
    val user = getUser()
    return locationRepository.getLocationInfo(user, locationId)
  }

  fun hasUserVisitedLocation(locationId: Int): Boolean {
    val client = getUserClient()
    return runBlocking {
      val response = client.get(ServerConfig.SERVER_URL + "/user/visited_locations")
      if (response.status.value == 200) {
        val visitedLocations = response.body<GetUserVisitedLocationsResponse>().visitedLocations
        return@runBlocking visitedLocations.any { it.id == locationId }
      }
      return@runBlocking false
    }
  }

  fun markLocationAsVisited(locationId: Int): Boolean {
    val client = getUserClient()
    val visitInfo = VisitAddRequest(locationId)
    return runBlocking {
      val response =
          client.post(ServerConfig.SERVER_URL + "/user/visited_locations") {
            contentType(ContentType.Application.Json)
            setBody(visitInfo)
          }
      return@runBlocking response.status.value == 200
    }
  }

  fun removeLocationFromVisited(locationId: Int): Boolean {
    val client = getUserClient()
    val visitInfo = VisitAddRequest(locationId)
    return runBlocking {
      val response =
          client.delete(ServerConfig.SERVER_URL + "/user/visited_locations") {
            contentType(ContentType.Application.Json)
            setBody(visitInfo)
          }
      return@runBlocking response.status.value == 200
    }
  }

  fun sendLocationEditSuggestionRequest(
      locationId: Int,
      name: String,
      shortDesc: String,
      longDesc: String
  ): Boolean {
    val client = getUserClient()
    val editSuggestion = LocationEditSuggestion(name, shortDesc, longDesc)
    return runBlocking {
      val response =
          client.post(ServerConfig.SERVER_URL + "/suggestions/locations/edit/" + locationId) {
            contentType(ContentType.Application.Json)
            setBody(editSuggestion)
          }
      return@runBlocking response.status.value == 200
    }
  }

  fun sendLocationAddRequest(
    name: String,
    lat: Double,
    long: Double,
    shortDesc: String,
    image: ByteArray
  ): Boolean {
    val client = getUserClient()
    val addSuggestion = LocationAddSuggestion(name = name, latitude = lat, longitude = long, shortDescription = shortDesc, wikipediaLink = null, image = image)
    return runBlocking {
      val response =
        client.post(ServerConfig.SERVER_URL + "/suggestions/locations/add") {
          contentType(ContentType.Application.Json)
          setBody(addSuggestion)
        }
      return@runBlocking response.status.value == 200
    }
  }
}
