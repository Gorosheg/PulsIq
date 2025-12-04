plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

applyCommonAndroid()

android {
    namespace = "gorosheg.pulsiq.common"
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