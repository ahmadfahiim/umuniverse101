plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services") // Firebase plugin
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.firebase"
    compileSdk = 35 // Use integer instead of a string for compileSdkVersion

    defaultConfig {
        applicationId = "com.example.firebase"
        minSdk = 24
        targetSdk = 34 // Match compileSdkVersion to avoid compatibility issues
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.core:core-ktx:1.9.0")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.7.0")) // Firebase BOM for version management
    implementation("com.google.firebase:firebase-crashlytics:18.4.1") // Optional: Add Firebase Crashlytics if needed
    implementation("com.google.firebase:firebase-auth:21.0.7") // Optional: Add Firebase Auth if needed
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-analytics:21.3.0")
//    implementation("com.google.android.gms:play-services-auth:20.1.0 ")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.firebase:firebase-firestore:24.5.0")
    implementation("com.squareup.picasso:picasso:2.8")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("com.google.firebase:firebase-storage:20.2.1")



}
