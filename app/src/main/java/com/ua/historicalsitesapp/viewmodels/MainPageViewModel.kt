package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.model.map.HsLocation
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete
import com.ua.historicalsitesapp.data.repository.locations.LocationDataSource
import com.ua.historicalsitesapp.data.repository.locations.LocationRepository

class MainPageViewModel(context: Context) : ViewModel() {

    private val locationRepository = LocationRepository(LocationDataSource())

    fun getAllLocations(): List<HsLocation> {
        return locationRepository.getAllLocations()
    }

    fun getLocationInfo(locationId: Int): HsLocationComplete {
        return locationRepository.getLocationInfo(locationId)
    }

}
