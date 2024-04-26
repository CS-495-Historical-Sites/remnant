## View Model for Authentication

### Overview

The `viewmodels/AuthViewModel` directory contains a view model for handling authentication. This includes performing registration, login, and logout

### View Model Methods

- **performRegistration**
    - Takes in user input and add a new user to the database after they have been authenticated.

- **isLoggedin**
    - Boolean for keeping track on if a user is logged in.

- **performLogin**
    - Takes in user input and validates the user input against existing data in the database.

- **performLogout**
    - Handles logging the user out and clearing cached tokens. It clears cached tokens so that the user is redirected to the login page on the next startup of the app.