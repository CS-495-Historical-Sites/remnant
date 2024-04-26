# Frequency Asked Questions

### How to you get the locations?

We retrieve our location information from Wikidata. The first step in the process is to query Wikidata for candidate locations.
This is done using this [custom search](https://w.wiki/96MG) on Wikidata. Then, we download the results from that query as a json file, that we use as a list of locations to scrape.

The script to pull the location information is in [Nim Datasource Scripts](https://github.com/CS-495-Historical-Sites/nim-datasource-scripts).

This script looks at the links in the `query.json` file and sends a request to Wikidata to retrieve the coordinates, image link, short description, and wikipedia page for the wikidata location. The script also links the NRHP number with a CSV file containing more information about NRHP locations. This is how we get the categories for each location.

### How is the app hosted?

The Android application is not "deployed". You can run it emulated using Android Studio, and it is possible to run it on an Android device if you follow [Android Studio's documentation](https://developer.android.com/studio/run/device).

The documentation for the admin website deployment is here -> [Admin Website Deployment](Admin_Website/Deployment.md)

The documentation for the backend deployment is here -> [Backend Deployment](Backend/Deployment.md)

### What are some external dependencies?

#### Backend

We are dependent on Postmark for sending welcome / email confirmation emails.

When the attempts to upload a photo to accompany a location add suggestion, the photo will be sent to AWS S3.
This makes the backend dependent on AWS.

More information about setting up backend dependencies can be found in the [Backend README](https://github.com/CS-495-Historical-Sites/remnant/blob/main/api/README.md)

#### Mobile App

We are dependent on [Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/overview).
Information about setting up the Google Maps API key can be found in [Android App Readme](More information about setting up backend dependencies can be found in the [Android README](https://github.com/CS-495-Historical-Sites/remnant/blob/main/android-app/README.md).
