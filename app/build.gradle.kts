plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "xm.space.ultimatememspace"
    compileSdk = 34

    defaultConfig {
        applicationId = "xm.space.ultimatememspace"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"

        //kotlinCompilerExtensionVersion '1.5.1'
        //        kotlinCompilerVersion '1.9.0'
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val navVersion = "2.7.2"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.microsoft.appcenter:appcenter-analytics:5.0.4")
    implementation("com.microsoft.appcenter:appcenter-crashes:5.0.4")

    //DI
    implementation("io.insert-koin:koin-androidx-compose:3.4.3")
    //Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    //Image
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.5")
    //PageIndicator
    implementation("com.google.accompanist:accompanist-pager-indicators:0.14.0")
    //QR-generator
    implementation("com.google.zxing:core:3.4.0")
    //Network
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.code.gson:gson:2.8.5")
    //Animation
    implementation("com.airbnb.android:lottie:6.0.1")

    implementation("com.google.accompanist:accompanist-pager:0.23.1")

    implementation("androidx.core:core-splashscreen:1.0.0")

    val cameraxVersion = "1.0.2"
    //CameraX
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-view:1.0.0-alpha29")

    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.accompanist:accompanist-permissions:0.19.0")
}