plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "gorosheg.pulsiq.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    api(libs.androidxCoreKtx)
    api(libs.androidxAppcompat)
    api(libs.androidxLifecycleRuntimeKtx)
    api(libs.androidxActivityCompose)
    api(platform(libs.androidxComposeBom))
    api(libs.androidxUi)
    api(libs.androidxUiGraphics)
    api(libs.androidxUiToolingPreview)
    api(libs.androidxMaterial3)
    api(libs.androidxMaterialIconsCore)
    api(libs.androidxLifecycleViewmodelCompose)
    api(libs.koinAndroid)
    api(libs.koinAndroidxCompose)
    api(libs.koinCore)

    api(libs.voyagerKoin)
    api(libs.voyagerNavigator)
    api(libs.voyagerTabNavigator)
    api(libs.voyagerTransitions)
    api(libs.accompanistPermissions)

    debugApi(libs.androidxUiTooling)
    debugApi(libs.androidxUiTestManifest)
}