plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.cortedemudaexataid"
    compileSdk = 34
    viewBinding{
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.cortedemudaexataid"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room components
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-guava:2.6.0")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    implementation("com.android.support:multidex:1.0.3")

    //RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.4")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    // QRCOde
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")

    // (Java only)
    implementation("androidx.work:work-runtime:2.8.1")

    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:2.8.1")

    implementation("com.karumi:dexter:6.2.3")


    implementation("com.jakewharton.rxbinding:rxbinding:1.0.1")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.4")

}