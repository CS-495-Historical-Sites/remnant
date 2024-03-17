package com.ua.historicalsitesapp.data.repository.locations

import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete

class LocationRepository(private val dataSource: LocationDataSource) {

  fun getLocationsNearPoint(
      user: LoggedInUser,
      point: LatLng,
      kilometerRadius: Float
  ): List<HsLocation> {
    return dataSource.getLocationsNearPoint(user, point, kilometerRadius)
  }

  fun getLocationInfo(user: LoggedInUser, locationId: Int): HsLocationComplete {
    return dataSource.getLocationInfo(user, locationId)
  }
}
