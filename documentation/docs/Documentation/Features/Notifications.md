# Notifications

#### Usage

Notifications are a permission required to use Geofences(currently no other
uses). Notification permissions are asked on the first login and if declined
must be activated in the device settings by going to the "Apps" section, then
"Notifications" settings and clicking "All Remnant notifications".

#### Development Modification

All notification related code is located in the functions
[notificationHelp()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L45),
[showNotification()](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt#L53)
in
[GeofenceBroadcastReceiver.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/geofence/GeofenceBroadcastReceiver.kt)
and the
[NotificationHelper](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/notifications/NotificationHelper.kt#L11)
class in
[NotificationHelper.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/notifications/NotificationHelper.kt).
For more information regarding creating and sending notifications on Android
follow the link here:
[Android Notification Documentation](https://developer.android.com/develop/ui/views/notifications).
