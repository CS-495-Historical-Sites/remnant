# User Profile Page

#### Usage

The User Profile Page displays info about the user. This information includes
username, email, and preferences gathered from registration or questionnaire.
You can edit the username and preferences by clicking on the pencil icon to the
right of the text box respectively.

#### Development Modification


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