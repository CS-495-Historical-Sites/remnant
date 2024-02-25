package com.ua.historicalsitesapp.data.repository.locations

import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete

class LocationRepository(private val dataSource: LocationDataSource) {
    fun getAllLocations(): List<HsLocation> {
        return dataSource.getAllLocations()
    }

    fun getLocationInfo(locationId: Int): HsLocationComplete {
        return dataSource.getLocationInfo(locationId)
    }
}