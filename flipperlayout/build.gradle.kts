apply(from = "../versions.gradle.kts")
apply(from = "../source.gradle.kts")

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
}

val compileVersion: Int by extra
val minVersion: Int by extra
val targetVersion: Int by extra
val buildVersion: String by extra
val kotlinVersion: String by extra
val appcompatVersion: String by extra
val libraryVersion: String by extra

android {
    compileSdkVersion(compileVersion)
    buildToolsVersion = buildVersion

    defaultConfig {
        minSdkVersion(minVersion)
        targetSdkVersion(targetVersion)
        versionCode = 1
        versionName = libraryVersion

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

apply(from = "https://raw.githubusercontent.com/nuuneoi/JCenter/master/installv1.gradle")
apply(from = "https://raw.githubusercontent.com/nuuneoi/JCenter/master/bintrayv1.gradle")

// To upload archives: ./gradlew clean bintrayUpload
bintray {
    val userName: String? by project
    val apiKey: String? by project

    user = userName
    key = apiKey

    // Other necessary properties are defined in source.gradle.kts
    // and assigned in the scripts applied above.
}
