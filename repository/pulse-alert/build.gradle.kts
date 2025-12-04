plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.pulse_alert"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:bluetooth"))
    implementation(project(":data:storage"))
}