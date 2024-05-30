plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.apollographql.apollo3") version "4.0.0-beta.6"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.exclusive"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.exclusive"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.2")
    // SDP (ScalableDP) and SSP (ScalableSP) libraries for handling scalable dimensions and spacing
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // Lifecycle components for managing Android lifecycle-aware components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // Lifecycle extensions for additional Android lifecycle support
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")


    // Kotlin coroutines for asynchronous programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // Room persistence library for database management
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")

    // Google Play services dependency for accessing location services
    implementation ("com.google.android.gms:play-services-location:21.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.libraries.places:places:3.3.0")

    // lottie
    implementation("com.airbnb.android:lottie:6.3.0")

    // Glide library
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")

    // CircleImageView library for displaying circular images
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Gson
    implementation ("com.google.code.gson:gson:2.9.0")

    // Retrofit And OkHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // RoundedImageView
    implementation ("com.makeramen:roundedimageview:2.3.0")

    //Apollo
    implementation("com.apollographql.apollo3:apollo-runtime:4.0.0-beta.6")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
}
kapt {
    correctErrorTypes = true
}
apollo {
    service("service") {
        packageName.set("com.example.exclusive")
        introspection {
            endpointUrl.set("https://mad44-android-sv-2.myshopify.com/api/2024-04/graphql.json")
            headers.put("api-key", "4bfbd5eac5ef87c5b2099fa60d740487")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}