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


- **Nearby Activity**
 This screen is designed to show you locations near you. We take a users location and display a list of the closest historical sites in a selected radius. You can save locations or get directions through Google maps. Users can adjust their radius to allow for more or less nearby locations and also search for specific locations.


- **Questionnaire Activity**
 This screen allows the app to gather preferences on topics relating to historical sites. It displays a question and then displays chips that you can select to choose your answers. Clicking on "Next" brings you to the next question until all questions are answered. 

 - **User Profile Page Activity**
 The User Profile Page displays info about the user. This information includes username, email, and preferences gathered from registration or questionnaire. You can edit the username and preferences by clicking on the pencil icon to the right of the text box respectively.




