### Feature: Using Geofences

### Caveats

- Geofences are only usable when Notification and Background location permissions are granted.
- From above: We do not request Background location permissions
- Only applicable to the first login since if the user declines notification permissions this must be changed in the settings app.
- App must be reloaded after allowing background location.

#### Context

The user accesses the maps screen for the first time and wants to be notified when near a location.
The user is also within 500 meters of any of the locations in the database.

#### Steps

1. The user logs in for the first time.
2. Then finishes the questionaire.
3. The user is prompted to allow notification permissions and accepts.
4. The user pushes the allow location permissions button and accepts.
5. The user must navigate to the settings app and grant background location permissions("Allow all the time").
6. Then refreshes the app to allow the geofencing client to create the geofences.
