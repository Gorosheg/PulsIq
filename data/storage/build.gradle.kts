plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.storage"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    implementation(libs.androidxDatastorePreferences)
    ksp(libs.roomCompiler)
}