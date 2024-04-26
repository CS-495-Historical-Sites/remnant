# Mobile App

## Overview

Our mobile app is an Android App. 

## Languages and libraries

The application is written in Kotlin, using the [Jetpack Compose](https://developer.android.com/develop/ui/compose) UI library.


## Build System and Development Environment

Android Studio is required. The Android SDK is required. Android Studio will walk you through installing the SDK. 

We are using Gradle as a build system. It integrates with Android Studio.

To set up your development environment, see the [development environment](Development_Environment.md) section.

### Screens

#### **For Users:**

**Nearby Activity**
 This screen is designed to show you locations near you. We take a users location when the home button is pressed and display a list of the closest historical sites in a selected radius. You can save locations or get directions through Google maps using the two buttons underneath the location card. Users can adjust their radius to allow for more or less nearby locations and also search for specific locations.


#### **For Devs:**
**Functions Overview**
This section provides a detailed breakdown of the key functions used within the nearby screen.

#### CheckScreen

  - **Purpose:** Requests location permissions to fetch user location data.
  - **Behavior:** Renders the page upon successful permission grant from the user.

#### FeedPage

  - **Purpose**: Fetches locations and handles location data.
  - **Parameters**:
      - `context`: Current context of the application.
      - `viewModel`: Used to track data.
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
      - Distance in milses `distance`
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

 

**Questionnaire Activity**
 This screen allows the app to gather preferences on topics relating to historical sites. It displays a question and then displays chips that you can select to choose your answers. Clicking on "Next" brings you to the next question until all questions are answered. 

**User Profile Page Activity**
 The User Profile Page displays info about the user. This information includes username, email, and preferences gathered from registration or questionnaire. You can edit the username and preferences by clicking on the pencil icon to the right of the text box respectively.


### View Models


View Models are used to store and UI related data and preserve it across different configurations such as screen changes. They also handle communication with the back end decoupling activities from data handling operations.


 - **AuthViewModel**
 

    AuthViewModel handles all the logic necessary for authorizing users. It contains functions that communicate with the backend for handling registration, login, and logout requests. 

 - **MainPageViewModel.kt**
    This view model handles all the functionality required to access location data. It contains functions to fetch nearby locations, suggest locations, allow a user to like or unlike locations, and update selected categories.

- **UserProfileViewModel**
    This view model handles data relating the user profile. It contains functions to update the username, location preferences, and retrieve user information for display on the profile page.




