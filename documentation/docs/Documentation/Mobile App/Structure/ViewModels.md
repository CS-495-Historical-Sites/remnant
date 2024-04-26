### View Models


View Models are used to store and UI related data and preserve it across different configurations such as screen changes. They also handle communication with the back end decoupling activities from data handling operations.


 - **AuthViewModel**
 
    AuthViewModel handles all the logic necessary for authorizing users. It contains functions that communicate with the backend for handling registration, login, and logout requests. 

 - **MainPageViewModel.kt**
    
    This view model handles all the functionality required to access location data. It contains functions to fetch nearby locations, suggest locations, allow a user to like or unlike locations, and update selected categories.

- **UserProfileViewModel**
    
    This view model handles data relating the user profile. It contains functions to update the username, location preferences, and retrieve user information for display on the profile page.



