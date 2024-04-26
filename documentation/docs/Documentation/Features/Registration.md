# Registration

#### Usage

This screen is designed to allow a user to create an account in our database. The activity has the user enter an email, username, and password to successfully register and account. If they have an account already they can navigate to the login activity.

#### Development Modification
The code for registration in Android Studio is located in 4 files. [RegistrationActivity.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/ui/screens/RegistrationActivity.kt) is used for the Android UI of the app. [AuthViewModel.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/viewmodels/AuthViewModel.kt) is used to make requests from the API for registration. [RegistrationResponse.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/data/model/auth/RegistrationResponse.kt), and [RegistrationDetails.kt](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/android-app/app/src/main/java/com/ua/historicalsitesapp/data/model/auth/RegistrationDetails.kt), are data classes used to group values together. 

The code for registration in the API is located in 3 files. [Auth.py](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/api/web/src/appl/auth.py), [Models.py](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/api/web/src/appl/models.py), and [User_Queries.py](https://github.com/CS-495-Historical-Sites/remnant/blob/docs/api/web/src/appl/remnant_db/user_queries.py).