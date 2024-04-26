### Google Maps View

#### Usage

Using the google maps screen in app is like any other map app. Zoom by pinching
or pulling with two fingers. Move around the map with one finger and tap on
locations to select.

#### Development Modification

The code is mostly within the Google Maps library component, but code for the page can be found at `android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt`.

The locations are sourced from [Get All Locations](../Backend/API Routes/Locations/Get Locations.md)

### Location Clustering

#### Usage

Location Clustering is an automatic function, as you zoom in and out the
locations group up as a way to declutter the screen.


#### Development Modification

The code for the clustering lives at `android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt` in the `CustomRendererClustering` function.

### Location Filtering

#### Usage

Location Filtering is located at the top of the maps screen clicking on any of
the filters opens the settings page and allows for filtering of locations based
on Military, Maritime History, Architecture, and more.

#### Development Modification

#### Android App

The code for the filter chips on the main view, and the settings side sheet can
be found at
`android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/CategoriesRow.kt`.
It interacts with the main page view model as a source truth accross UI
components.

#### Backend

Information on how the locations are classified can be found in the FAQ sections
