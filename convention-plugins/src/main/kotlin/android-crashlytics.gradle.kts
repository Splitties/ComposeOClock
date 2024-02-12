plugins {
    kotlin("android")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

dependencies {
    implementation {
        platform(Firebase.bom)()
        Firebase.analyticsKtx()
        Firebase.crashlyticsKtx()
    }
}
