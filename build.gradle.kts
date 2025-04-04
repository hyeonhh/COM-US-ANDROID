// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

}

buildscript {
    repositories {
        google()
    }
    dependencies {
        @Suppress("ktlint:standard:property-naming")
        val nav_version = "2.8.8"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}