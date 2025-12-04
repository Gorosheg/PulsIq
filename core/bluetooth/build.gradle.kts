plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.bluetooth"
}

dependencies {
    implementation(project(":core:common"))
}