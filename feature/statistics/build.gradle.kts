plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.statistics"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":repository:statistics"))

    implementation(libs.vicoCore)
    implementation(libs.vicoCompose)
    implementation(libs.vicoComposeM3)
    implementation(libs.androidxComposeUiViewbinding)

    implementation(libs.androidxUiToolingPreview)
    debugImplementation(libs.androidxUiTooling)
}