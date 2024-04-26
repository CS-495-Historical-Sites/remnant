# Location Suggestions and Edits

## Usage (User)

### Suggestions

Users can suggest a location by navigating the google map view, opening the
modal on the left, and selecting sugggest location. A pin will then appear on
the users screen, enabling them to select where they would like to place their
location.

After pressing "Place", they will be prompted to provide a name, short
description, and an image for the location. They can cancel, or submit their
suggestion.

### Edits

Users can provide a location edit suggestion by clicking on the pencil icon in
the top right of the location info card. They can cancel, or submit their
suggestion.

### Development Modification

#### Android App

Both the location edit suggestion forms can be found as components in
`android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/`.

The edit suggestion is prompted from
`android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/LocationInfoCardContent.kt`.

The location suggestion is prompted from
`android-app/app/src/main/java/com/ua/historicalsitesapp/ui/components/GoogleMapsScreen.kt`

#### Backend

### Usage (Admin)

After navigating to our [Admin Website](https://app.uahistoricalsites.com/),
only admins (via allowlisted and confirmed email addresses) can view users
suggestions. They can click approve or deny on users suggestions after viewing
their details.
