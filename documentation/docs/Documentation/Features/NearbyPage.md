### Nearby Page

#### Usage

This screen is designed to show you locations near you. We take a users location when the home button is pressed and display a list of the closest historical sites in a selected radius. You can save locations or get directions through Google maps using the two buttons underneath the location card. Users can adjust their radius to allow for more or less nearby locations and also search for specific locations.


#### Development Modification


**Functions Overview:**
 - This section provides a detailed breakdown of the key functions used within the nearby screen.

#### CheckScreen

  - **Purpose:** Requests location permissions to fetch user location data.
  - **Behavior:** Renders the page upon successful permission grant from the user.

#### FeedPage

  - **Purpose**: Fetches locations and handles location data.
  - **Parameters**:
      -  Current context of the application `context`
      -  View model to track data `viewModel`
  - **Default Settings**:
      - Search Radius: 16.09 kilometers (approximately 10 miles).
      - Search Query: Empty by default.
      - Total Locations: Empty List
      - Displayed Locations: Empty List
  - **List Handling**:
      - Total list of locations displayed.
      - Locations loaded per request (`loadMore` variable): 20.
  - **Behavior**:
      - Requests a list of locations within the specified search radius.
      - Displays "No results found" message if no locations are returned.
      - Uses `calculateDistanceinMiles` function to convert distance from the user to miles.
      - Loops through each location and calls `HomeMainContent` to display location information.

#### HomeMainContent

  - **Purpose**: Decorates each location card displayed in the feed.
  - **Parameters**: 
      - Receives a variable containing all card information (images, title, description, liked status, etc.) `locationInfo`
      - Distance in miles `distance`
      - View model `view`.
  - **Features**:
      - Two buttons per card:
          - Directions: Links to Google Maps with location coordinates.
          - Like: Updates liked status on the backend.
      - Styling: Font styles and other visual details are defined within this function.

#### SearchBar

  - **Purpose**: Handles the appearance and functionality of the search bar.
  - **Parameters** 
      - Current Query `searchQuery`
  - **Customization**: Allows adjustments to size, color, and overall style.


#### HomeAppBar

  - **Purpose**: Manages the top bar of the page.
  - **parameters**: 
      - The number of displayed locations `displayCount`
      - The number of total locations `totalCount`
      -  Current radius `radius`.
  - **Features**:
      - Displays the count of rendered location cards and the total number of nearby locations.
      - Includes a filter button to update the search radius (options: 5, 10, 25, 50 kilometers).
      - Converts distance to kilometers for compatibility with the backend.

