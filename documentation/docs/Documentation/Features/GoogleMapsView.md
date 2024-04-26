### Google Maps View

#### Usage

Using the google maps screen in app is like any other map app. Zoom by pinching
or pulling with two fingers. Move around the map with one finger and tap on
locations to select.

#### Development Modification

### Location Clustering

#### Usage

Location Clustering is an automatic function, as you zoom in and out the
locations group up as a way to declutter the screen.

#### Development Modification

### Location Filtering

#### Usage

Location Filtering is located at the top of the maps screen clicking on any of
the filters opens the settings page and allows for filtering of locations based
on Heritage, Maritime History, Development, among others.

#### Development Modification

#### Android APp

The code for the filter chips on the main view, and the settings side sheet can
be found at
`android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/CategoriesRow.kt`.
It interacts with the main page view model as a source truth accross UI
components.
