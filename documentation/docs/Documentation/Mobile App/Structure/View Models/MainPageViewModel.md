## View Model for the Main Page

### Overview

The `viewmodels/MainPageViewModel` directory contains a view model for handling the categories on the main page. It has a list of categories which can filter nearby locations.

### ViewModel Methods

- **updateCategoryState**

    - Used to update filter state when categories have been changed.

- **getUser**
    - Returns the currently logged-in user data model.

- **getUserClient**
    - Constructs an `HttpClient` configured with the user's credentials for API requests.

- **getHistoricalLocationNearPoint**
    - Fetches historical sites within a specified radius of a given point based on user location.

- **filterClusterItems**
    - Filters cluster items based on the inclusion or exclusion of categories selected by the user.

- **getLocationInfo**
    - Retrieves more information about a specific location based on the location ID.

- **hasUserVisitedLocation**
    - Checks whether a user has visited a specific location by sending a GET request to the server.

- **markLocationAsVisited**
    - Sends a POST request to mark a specific location as visited on the user's profile.

- **removeLocationFromVisited**
    - Sends a DELETE request to remove a location from the user's list of visited sites.

- **sendLocationEditSuggestionRequest**
    - Submits an edit suggestion for a specific location, including name and descriptions, through a POST request.

- **sendLocationAddRequest**
    - Adds a new location to the system by sending location details and an image in a POST request.

