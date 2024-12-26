plugins {
    id("com.android.application") version "8.7.3"  // Ensure the version is specified
    id("org.jetbrains.kotlin.android") version "1.7.10"  // Align Kotlin version to 1.7.10 or later
    id("org.jetbrains.kotlin.kapt") version "1.7.10"  // Align Kotlin version to 1.7.10 or later
    id("com.google.dagger.hilt.android") version "2.48"
    id("org.jetbrains.compose") version "1.7.3"  // Ensure version is 1.7.3 to match Compose Multiplatform version
}


tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)  // Use layout.buildDirectory instead of buildDir
}

repositories {
    google()  // This should already be correct for most setups
    maven { url = uri("https://maven.google.com/") } // Explicitly add the new URL
    mavenCentral()
    maven("https://jetbrains.bintray.com/compose") // Use this alternative URL
}

dependencies {
    implementation("androidx.compose.material3:material3:1.3.1")  // Use the correct version
    // Add other dependencies as needed
}