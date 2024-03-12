package com.ua.historicalsitesapp.data.repository.locations

import com.google.android.gms.maps.model.LatLng
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete

class LocationRepository(private val dataSource: LocationDataSource) {
  fun getAllLocations(): List<HsLocation> {
    return dataSource.getAllLocations()
  }

  fun getLocationsNearPoint(point: LatLng, kilometerRadius: Float): List<HsLocation> {
    return dataSource.getLocationsNearPoint(point, kilometerRadius)
  }

  fun getLocationInfo(locationId: Int): HsLocationComplete {
    return dataSource.getLocationInfo(locationId)
  }
}
