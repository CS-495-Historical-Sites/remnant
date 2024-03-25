plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  kotlin("plugin.serialization") version "1.9.21"
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  id("com.ncorti.ktfmt.gradle") version "0.17.0"
}

secrets {
  // Optionally specify a different file name containing your secrets.
  // The plugin defaults to "local.properties"
  propertiesFileName = "secrets.properties"

  // A properties file containing default secret values. This file can be
  // checked in version control.
  defaultPropertiesFileName = "local.defaults.properties"

  // Configure which keys should be ignored by the plugin by providing regular expressions.
  // "sdk.dir" is ignored by default.
  ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
  ignoreList.add("sdk.*") // Ignore all keys matching the regexp "sdk.*"
}

android {
  namespace = "com.ua.historicalsitesapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.ua.historicalsitesapp"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions { jvmTarget = "1.8" }
  buildFeatures {
    compose = true
    viewBinding = true
  }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.1" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

val ktor_version: String by project
val nav_version = "2.7.7"

dependencies {
  implementation("androidx.navigation:navigation-compose:$nav_version")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.material3:material3:1.2.1")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("com.google.android.gms:play-services-maps:18.2.0")
  implementation("androidx.navigation:navigation-compose:2.7.7")
  implementation("com.google.android.gms:play-services-location:21.2.0")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  debugRuntimeOnly("androidx.compose.ui:ui-test-manifest")
  implementation("io.ktor:ktor-client-core:$ktor_version")
  implementation("io.ktor:ktor-client-cio:$ktor_version")
  implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
  implementation("io.ktor:ktor-client-auth:$ktor_version")
  implementation("com.google.maps.android:maps-compose-utils:4.3.2")
  implementation("io.coil-kt:coil-compose:2.5.0")
  implementation("androidx.compose.animation:animation-android:1.6.2")
  implementation("androidx.compose.foundation:foundation-android:1.6.2")
  implementation("androidx.compose.foundation:foundation-layout-android:1.6.2")
  implementation("androidx.compose.material:material-icons-core-android:1.6.2")
  implementation("androidx.compose.runtime:runtime-android:1.6.2")
  implementation("androidx.compose.ui:ui-android:1.6.2")
  implementation("androidx.compose.runtime:runtime-saveable-android:1.6.2")

  implementation("androidx.compose.ui:ui-graphics-android:1.6.2")
  implementation("androidx.compose.ui:ui-text-android:1.6.2")
  implementation("androidx.compose.ui:ui-unit-android:1.6.2")
  implementation("androidx.datastore:datastore-core:1.0.0")
  implementation("androidx.datastore:datastore-preferences-core:1.0.0")
  implementation("androidx.navigation:navigation-common:2.7.7")
  implementation("com.google.maps.android:android-maps-utils:3.8.0")
  implementation("com.google.maps.android:maps-compose:4.3.2")
  implementation("io.coil-kt:coil-compose-base:2.5.0")
  implementation("io.ktor:ktor-http-jvm:2.3.8")
  implementation("io.ktor:ktor-serialization-jvm:2.3.8")
  implementation("io.ktor:ktor-utils-jvm:2.3.8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.6.2")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.2")
  implementation("androidx.compose.material:material-icons-extended-android:1.6.2")
  implementation("com.google.accompanist:accompanist-flowlayout:0.20.0")
  implementation("androidx.lifecycle:lifecycle-common:2.7.0")
  //noinspection KtxExtensionAvailable
  implementation("androidx.activity:activity:1.8.2")
  //noinspection KtxExtensionAvailable
  implementation("androidx.core:core:1.12.0")
  //noinspection KtxExtensionAvailable
  implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
  //noinspection KtxExtensionAvailable
  implementation("androidx.navigation:navigation-runtime:2.7.7")
  // Preferences DataStore (SharedPreferences like APIs)
  dependencies { implementation("androidx.datastore:datastore-preferences:1.0.0") }
}
