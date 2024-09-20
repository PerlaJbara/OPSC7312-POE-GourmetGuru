plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            storeFile =
                file("release-key-2.jks")
            storePassword = "snowqueen46*"
            keyAlias = "releasekey"
            keyPassword = "bloonstd6isawesomelol4321*"
        }
    }
    namespace = "com.opsc7311poe.gourmetguru_opscpoe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.opsc7311poe.gourmetguru_opscpoe"
        minSdk = 27
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" //used to be 1.8 for future ref, also i edited the grade properties
    }

    // Data Binding Configuration
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
