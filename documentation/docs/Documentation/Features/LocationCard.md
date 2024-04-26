### Location Card

### Usage

Information cards are popups after selecting a historical location. They provide
a picture, name, a short descritpion, and a button to route the user from their
current location.

### Development Modification

#### Android

Files to edit the location info card can be found at

-   `android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/LocationInfoCard.kt`
-   `android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/LocationInfoCardContent`

#### Backend

The route calls [Get Location](../Backend/API Routes/Locations/Get Location.md)
to get the location information to display on the app.
