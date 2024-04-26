## UI Screens

### Overview

The `ui/screens` directory contains UI screens that displays the main
functionaility of the app.

### Screen Details

-   **NearbyActivity.kt**

This screen is designed to show you locations near you. We take a users location
and display a list of the closest historical sites in a selected radius. You can
save locations or get directions through Google maps. Users can adjust their
radius to allow for more or less nearby locations and also search for specific
locations.

-   **QuestionnaireActivity.kt**

This screen allows the app to gather preferences on topics relating to
historical sites. It displays a question and then displays chips that you can
select to choose your answers. Clicking on "Next" brings you to the next
question until all questions are answered.

-   **UserProfilePage.kt**

The User Profile Page displays info about the user. This information includes
username, email, and preferences gathered from registration or questionnaire.
You can edit the username and preferences by clicking on the pencil icon to the
right of the text box respectively.

-   **LoginActivity.kt**

This screen is designed to allows users to access their accounts by providing
their credentials. By providing the user's email and password, the activity
verifies the entered email/password against stored credentials in the database.
If the user does not have an account they can navigate to the registration
activity.

It is also the landing page for when the user recieves a 401 error.

-   **RegistrationActivity.kt**

This screen is designed to allow a user to create an account in our database.
The activity has the user enter an email, username, and password to successfully
register and account. If they have an account already they can navigate to the
login activity.

-   **MainPageActivity.**

This screen is our google maps screen, as well as its popups and side bars.
