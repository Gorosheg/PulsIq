plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("pulsiq.moscow-time")
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq"

    defaultConfig {
        applicationId = "gorosheg.pulsiq"
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":repository:pulse-alert"))
    implementation(project(":core:bluetooth"))
    implementation(project(":core:common"))
    implementation(project(":data:storage"))
    implementation(project(":feature:monitoring"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:statistics"))
    implementation(project(":feature:pulse-notification"))
    implementation(project(":feature:device-connection"))
    implementation(project(":repository:statistics"))
    implementation(libs.androidxLifecycleProcess)
}