plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.statistics"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":data:storage"))
}