package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.auth.GetBearerTokens
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.data.model.visits.GetUserVisitedLocationsResponse
import com.ua.historicalsitesapp.data.model.visits.VisitAddRequest
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.repository.locations.LocationDataSource
import com.ua.historicalsitesapp.data.repository.locations.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

class MainPageViewModel(context: Context) : ViewModel() {

    private val locationRepository = LocationRepository(LocationDataSource())

    private val loginRepository =
        LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

    private fun getUser(): LoggedInUser {
        return loginRepository.user ?: throw Exception("MainPageViewModel could not retrieve user")
    }

    fun getUserClient(): HttpClient {
        val user = getUser()
        var usertokens = GetBearerTokens(user)
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        usertokens
                    }
                    refreshTokens {
                        val refreshTokenInfo: LoggedInUser = client.post(
                            urlString = "http://10.0.2.2:8080/api/refresh"
                        ) { markAsRefreshTokenRequest() }.body()
                        usertokens = GetBearerTokens(refreshTokenInfo)
                        usertokens
                    }
                }
            }
        }
        return client
    }

    fun getAllLocations(): List<HsLocation> {
        return locationRepository.getAllLocations()
    }

    fun getLocationInfo(locationId: Int): HsLocationComplete {
        return locationRepository.getLocationInfo(locationId)
    }

    fun hasUserVisitedLocation(locationId: Int): Boolean {
        val client = getUserClient()
        return runBlocking {
            val response = client.get("http://10.0.2.2:8080/api/user/visited_locations") {
                contentType(ContentType.Application.Json)
            }
            if (response.status.value == 200) {
                val visitedLocations =
                    response.body<GetUserVisitedLocationsResponse>().visitedLocations
                return@runBlocking visitedLocations.any { it.id == locationId }
            }
            return@runBlocking false;
        }
    }

    fun markLocationAsVisited(locationId: Int): Boolean {
        val client = getUserClient()
        val visitInfo = VisitAddRequest(locationId)
        return runBlocking {
            val response = client.post("http://10.0.2.2:8080/api/user/visited_locations") {
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
            val response = client.delete("http://10.0.2.2:8080/api/user/visited_locations") {
                contentType(ContentType.Application.Json)
                setBody(visitInfo)
            }
            return@runBlocking response.status.value == 200
        }
    }

}
