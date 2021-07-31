apply(from = "../versions.gradle.kts")
apply(from = "../source.gradle.kts")

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}

val compileVersion: Int by extra
val minVersion: Int by extra
val targetVersion: Int by extra
val buildVersion: String by extra
val kotlinVersion: String by extra
val appcompatVersion: String by extra
val libraryVersion: String by extra
val publishedGroupId: String by extra
val libraryName: String by extra

android {
    compileSdk = compileVersion
    buildToolsVersion = buildVersion

    defaultConfig {
        minSdk = minVersion
        targetSdk = targetVersion

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

    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1") {
        exclude(group = "com.android.support", module = "support-annotations")
    }

    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    testImplementation("junit:junit:4.12")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
}

// Disable javadoc
tasks.withType(Javadoc::class.java) {
    enabled = false
}