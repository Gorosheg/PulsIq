plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.pulse_notification"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:bluetooth"))
    implementation(project(":core:ui"))
}