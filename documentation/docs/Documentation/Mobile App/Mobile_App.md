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


### Nearby Activity
 This screen is designed to show you locations near you. We take a users location when the home button is pressed and display a list of the closest historical sites in a selected radius. You can save locations or get directions through Google maps using the two buttons underneath the location card. Users can adjust their radius to allow for more or less nearby locations and also search for specific locations.


### **For Devs:**
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

##

### **For Users:**


### Questionnaire Activity
 This screen allows the app to gather preferences on topics relating to historical sites. It displays a question and then displays chips that you can select to choose your answers. Clicking on "Next" brings you to the next question until all questions are answered. 


### **For Devs:**
**Functions Overview:**
 - This section provides a detailed breakdown of the key functions used within the questionnaire activity.

#### QuestionnaireMenu

  - **Purpose**: Stores a list of questions that will be displayed to the user.
  - **Parameters**:
      -  When the questionnaire is completed, pass all answers to the callback `onQuestionnaireCompleted`
  - **Questions**:
      - Questions are stored in a list of lists. The outer lists are the questions and the inner lists are the answers. Can be modified to contain more or less questions and answers.
  - **Behavior**:
      - Stores questionnaire answers as the user clicks `Next`.
      - Once the current question index is greater than or equals the question list size, it calls `onQuestionnaireCompleted` to complete the activity.

#### QuestionnaireCard

  - **Purpose**: Used for displaying the UI for the questionnaire page
  - **Parameters**:
      -  `currentQuestionIndex` keeps track of what question the user is answering.
      -  `question` String that displays the current question
      -  `options` keeps track of what chips/answers the user is choosing.
      -  `selectedOptions` keeps track of all the chips the user is choosing.
      -  `onAnswer` stores the chips with their respective question.
      -  `onNext` Controls the button functionality.
      
  - **Behavior**:
      - Cycles through each question and displays the answers as chips.
      - When the `Next` button is clicked it displays the next question and answers.
      - When the current question index reaches the list size it changes the button to `Finish`. Clicking on `Finish` will send the user to the main activity.

## 

### **For Users:**


### User Profile Page Activity
 The User Profile Page displays info about the user. This information includes username, email, and preferences gathered from registration or questionnaire. You can edit the username and preferences by clicking on the pencil icon to the right of the text box respectively.


### **For Devs:**
**Functions Overview:**
 - This section provides a detailed breakdown of the key functions used within the user profile page. Many of the functions on this page are UI elements. Documentation for these elements can be found on Material Design's website: https://m3.material.io/.

#### TopBar

  - **Purpose:** Displays the Remannt bar are the top of the screen.

#### UserNameTextField

  - **Purpose**: Fetches username and displays it as an outlined text box.       Additional functionality has been added to support editing the username and updating it on the backend

  - **Parameters**:
      -  Username obtained from the database `fetchedUsername`
      -  Updates typed username on profile page as it is edited `onUserNameChange`.
      -  On click functionality for the save button `onSaveClick`
      -  Boolean used to display other UI when the username is being edited `isEditing`
      -  On click functionality for when the edit button has been clicked `onTextFieldClicked`.
  - **Behavior**:
      - Displays the username on activity launch.
      - The edit button can be clicked to bring up an editable text field and the save button.
      - The save button will send the new username to be validated in the backend.

#### EmailTextField

  - **Purpose**: Displays the email of the user.
  - **Parameters**: 
      - Recieves the email from the database `email`
      - Used to modify the UI design `modifier`
  - **Features**:
      - Displays the email in an unclickable text field.

#### Save Button

  - **Purpose**: Handles updating the username on the profile page.
  - **Parameters** 
      - Button functionality: `onClick`
      - Controls the visibility of the save button.`isVisible`
  - **Features**:
      - Displays the save button when the edit button of the `UsernameTextField` is clicked.
      - Stays visible until the save button is clicked on canceled by another action.

#### Logout Button

  - **Purpose**: UI for the logout button
  - **Parameters** 
      - Button functionality: `onClick`
  - **Features**:
      - Displays the logout button. This is purely UI.

#### Logout Button

  - **Purpose**: UI for displaying chips in the preferences function.
  - **Parameters** 
      - String for the answers to be displayed `text`.
  - **Features**:
      - UI for the chips. `text` is used to display the answers on the chips.

#### Preferences

  - **Purpose**: Displays the preferences gathered from the questionnaire page.
  - **parameters**: 
      - Handles UI functionality when the text box is clicked `onPreferencesToggle`.
      - Handles the up or down arrow functionality `isPreferencesExpanded`.
      - Variable for the questions and answers from the questionnaire page `fetchedAnswers`.
      - Handles edit button functionality `onEditClick`.
  - **Features**:
      - Displays the questions and answers obtained from the questionnaire page.
      - Clicking on the preferences box opens up a drop-down displaying the questions and answers. Clicking again closes the drop-down box.
      - Has an edit button used to display an alert dialog. The alert dialog has a cancel or confirm button. Clicking cancel will cancel the action, clicking confirm will bring the user to the questionnaire page.
      
#### ShowEditConfirm

  - **Purpose**: Alert Dialog box to be used in the `Preferences` function
  - **Parameters** 
      - Boolean to see if confirm is clicked or not `showEditConfirmation`.
  - **Features**:
      - UI for the alert dialog box. Displays text asking "Are you sure you want to edit your preferences?" Has Confirm and Cancel buttons that change the `showEditConfirmation` boolean.

#### UserProfileCard

  - **Purpose**: Main UI for the profile page.
  - **Parameters** 
      - All parameters are explained in their functions above.
  - **Features**:
      - Main UI for the profile page. Handles spacing, scrolling, column placement, and displaying of UI elements.   

#### UserProfilePage

  - **Purpose**: Calls the `UserProfileCard` and handles launch effects.
  - **Parameters** 
      - All parameters are explained in their functions above.
  - **Features**:
      - Main handler for the profile page. Calls UserProfileCard and has Launch effects to make sure parameters are obtained when entering the page.

##

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




