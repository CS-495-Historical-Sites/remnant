## View Model for the User Profile Page

### Overview

The `viewmodels/UserProfileViewModel` directory contains a view model for handling the user profile page. This includes logout, getting user information, getting user preferences, and updating user info.

### View Model Methods

- **submitRegistrationQuestionnaire**
  - For use in `ui/screens/QuestionnaireActivity.kt`.

  - Stores the answers from the questionnaire page in the database.


- **getProfileInfo**
  - For use in `ui/screens/UserProfilePage.kt`.

  - Gets the user profile info (username, email, and questionnaire answers) for use in displaying it on the profile page

- **updateUsername**
  - For use in `ui/screens/UserProfilePage.kt`.

  - Used for updating the username from the profile page. Send the new username to the database. Replaces the old username with the new username.