plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.monitoring"
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:bluetooth"))
    implementation(project(":core:ui"))
    implementation(project(":repository:statistics"))
    implementation(project(":feature:device-connection"))

    testImplementation(libs.junitJupiterApi)
    testRuntimeOnly(libs.junitJupiterEngine)
}
