plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.dailyquoteapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dailyquoteapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation ("androidx.work:work-runtime-ktx:2.9.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")


    // Firebase - Use the LATEST BOM version
//    implementation(platform("com.google.firebase:firebase-bom:33.4.0")) // or use the latest stable version

    // Firebase dependencies (BOM will manage versions)


        // Firebase BoM (manages versions automatically)
        implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

        // Firestore
        implementation("com.google.firebase:firebase-firestore-ktx")

        // Auth
        implementation("com.google.firebase:firebase-auth-ktx")




    implementation(libs.androidx.activity)
    implementation(libs.androidx.swiperefreshlayout)
//    implementation(libs.firebase.auth.ktx)
//    implementation(libs.firebase.firestore.ktx)


}
