plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.ikrom.music_club_classic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ikrom.music_club_classic"
        minSdk = 26
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
}

dependencies {
    implementation(libs.bundles.dagger)
    kapt(libs.bundles.dagger.compiler)
    implementation(libs.miniequalizer)
    implementation(libs.bundles.glide)
    implementation(libs.fido)
    implementation(libs.bundles.media3)
    implementation(libs.palette)
    implementation(libs.volley)
    implementation(libs.percentlayout)
    implementation(libs.livedata)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(project(":feature:explore"))
    implementation(project(":core:appbar"))
    implementation(project(":feature:search"))
    implementation(project(":core:player"))
    implementation(project(":core:ui"))
    implementation(project(":data:youtube"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
