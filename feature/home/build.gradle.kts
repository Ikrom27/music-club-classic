plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "ru.ikrom.home"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    api(project(":core:base-fragment"))
    implementation(project(":common:player-handler"))
    implementation(project(":component:appbar"))
    implementation(project(":common:adapter-delegates"))
    implementation(project(":data:youtube"))
    implementation(libs.core.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.bundles.dagger)
    implementation(project(":core:theme"))
    kapt(libs.bundles.dagger.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}