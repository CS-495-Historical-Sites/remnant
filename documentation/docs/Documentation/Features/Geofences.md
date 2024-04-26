# Geofences

#### Usage

Geofences are a way to recieve notifications when close to one of the historical
locations. Geofences are only active when notifications and background location
services are enabled. Background location services must be activated in the
devices settings by going to the "Apps" section followed by "Permission"
settings, then "Location" settings and click "Allow all the time".

#### Development Modification

#### Android

To Modify the Geofencing implementation all code related to Geofencing is
located in the functions
[GoogleMapsScreen()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt#L159),
[getGeofencingRequest()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt#L339)
in
[GoogleMapsScreen.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt)
and the class
[GeofenceBroadcastReceiver](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L20)
in
[GeofenceBroadcastReceiver.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt).
For more information regarding Geofences, the Geofencing Client, and the
Geofencing API on Android follow the link here:
[Android Geofence Documentation](https://developer.android.com/develop/sensors-and-location/location/geofencing).

#### Backend

The locations are sourced from [Get All Locations](../Backend/API
Routes/Locations/Get Locations.md)
