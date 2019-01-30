apply(from = "../versions.gradle.kts")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

val compileVersion: Int by extra
val minVersion: Int by extra
val targetVersion: Int by extra
val buildVersion: String by extra
val kotlinVersion: String by extra
val appcompatVersion: String by extra

android {
    compileSdkVersion(compileVersion)
    defaultConfig {
        applicationId = "com.juniperphoton.showcase"
        minSdkVersion(minVersion)
        targetSdkVersion(targetVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to arrayOf("*.jar"))))
    implementation(project(":flipperlayout"))
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    testImplementation("junit:junit:4.12")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
}