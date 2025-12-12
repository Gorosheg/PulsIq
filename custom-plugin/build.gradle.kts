plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("moscowTime") {
            id = "pulsiq.moscow-time"
            implementationClass = "pulsiq.gradle.MoscowTimePlugin"
            displayName = "Moscow Time Plugin"
            description = "Fetches and logs local time in Moscow using a public API"
        }
    }
}
