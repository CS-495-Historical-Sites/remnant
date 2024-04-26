# Features

### Registration

#### Usage


#### Development Modification


### Logging in

#### Usage


#### Development Modification


### Google Maps

#### Usage

Using the google maps screen in app is like any other map app. Zoom by pinching or pulling with two fingers. Move around the map with one finger and tap on locations to select.

#### Development Modification


### Information Cards

#### Usage

Information cards are popups after selecting a historical location. They provide a picture, name, a short descritpion, and a button to route the user from their current location.

#### Development Modification


### Location Clustering

#### Usage
Location Clustering is an automatic function, as you zoom in and out the locations group up as a way to declutter the screen.

#### Development Modification


### Location Filtering

#### Usage
Location Filtering is located at the top of the maps screen clicking on any of the filters opens the settings page and allows for filtering of locations based on Heritage, Maritime History, Development, among others. 

#### Development Modification

### Notifications

#### Usage
Notifications are a permission required to use Geofences(currently no other uses). Notification permissions are asked on the first login and if declined must be activated in the device settings by going to the "Apps" section, then "Notifications" settings and clicking "All Remnant notifications".

#### Development Modification
All notification related code is located in the functions [notificationHelp()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L45), [showNotification()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L53) in [GeofenceBroadcastReceiver.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt) and the [NotificationHelper](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/notifications/NotificationHelper.kt#L11) class in [NotificationHelper.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/notifications/NotificationHelper.kt). For more information regarding creating and sending notifications on Android follow the link here: [Android Notification Documentation](https://developer.android.com/develop/ui/views/notifications).

### Geofences

#### Usage
Geofences are a way to recieve notifications when close to one of the historical locations. Geofences are only active when notifications and background location services are enabled. Background location services must be activated in the devices settings by going to the "Apps" section followed by "Permission" settings, then "Location" settings and click "Allow all the time".

#### Development Modification
To Modify the Geofencing implementation all code related to Geofencing is located in the functions [GoogleMapsScreen()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt#L159), [getGeofencingRequest()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt#L339) in [GoogleMapsScreen.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt) and the class [GeofenceBroadcastReceiver](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L20) in [GeofenceBroadcastReceiver.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt). For more information regarding Geofences, the Geofencing Client, and the Geofencing API on Android follow the link here: [Android Geofence Documentation](https://developer.android.com/develop/sensors-and-location/location/geofencing).



### Nearby Page

#### Usage

#### Development Modification


### Profile Page

#### Usage

#### Development Modification
