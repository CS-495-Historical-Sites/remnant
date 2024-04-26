# Mobile App

## Overview

Our mobile app is an Android App. 

## Languages and libraries

The application is written in Kotlin, using the [Jetpack Compose](https://developer.android.com/develop/ui/compose) UI library.


## Build System and Development Environment

Android Studio is required. The Android SDK is required. Android Studio will walk you through installing the SDK. 

We are using Gradle as a build system. It integrates with Android Studio.

To set up your development environment, see the [development environment](Development_Environment.md) section.

## Screens

### **For Users:**




**Questionnaire Activity**




**Login Activity**
 This screen is designed to allows users to access their accounts by providing their credentials. By providing the user's email and password, the activity verifies the entered email/password against stored credentials in the database. If the user does not have an account they can navigate to the registration activity. 

**Registration Activity**
 This screen is designed to allow a user to create an account in our database. The activity has the user enter an email, username, and password to successfully register and account. If they have an account already they can navigate to the login activity.

**Main Page Activity**
 This screen is designed to show you locations near you. We take a users location and display a list of the closest historical sites in a selected radius. You can save locations or get directions through Google maps. Users can adjust their radius to allow for more or less nearby locations and also search for specific locations.


### View Models


View Models are used to store and UI related data and preserve it across different configurations such as screen changes. They also handle communication with the back end decoupling activities from data handling operations.


 - **AuthViewModel**
 
    AuthViewModel handles all the logic necessary for authorizing users. It contains functions that communicate with the backend for handling registration, login, and logout requests. 

 - **MainPageViewModel.kt**
    
    This view model handles all the functionality required to access location data. It contains functions to fetch nearby locations, suggest locations, allow a user to like or unlike locations, and update selected categories.

- **UserProfileViewModel**
    
    This view model handles data relating the user profile. It contains functions to update the username, location preferences, and retrieve user information for display on the profile page.




