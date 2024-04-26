#### How to run the project

- Clone project
- Open with Android Studio IDE
- Set `ANDROID_HOME` variable within Android Studio or create a file named `local.properties` and add `sdk.dir=<your_android_sdk_location>`
- Install an emulation device
- Press "run"
  - This version of the project will send requests to our server hosted in AWS, so you do not need
    to set up the backend separately. To connect to the backend running locally, edit the file
    at `app/src/main/java/com/ua/historicalsitesapp/util/ServerConfig.kt` so
    that `SERVER_URL = TEST_SERVER_URL`

#### How to format the project

In the `./android-app` directory, run `./gradlew ktfmtFormat`

#### Setting up Google Maps

The app will launch a blank screen without an appropriate Google Maps API key. You can create an account to get one on [Google Maps Platform](https://developers.google.com/maps).

1. Create a file named `secrets.properties`.
2. Inside the `secrets.properties` file add `MAPS_API_KEY="YOUR_API_KEY"`.

Replace `YOUR_API_KEY` with your actual key.

You can access your API key in the [Google Maps Platform](https://developers.google.com/maps) under Keys & Credentials.
