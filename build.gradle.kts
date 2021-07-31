import java.net.URI

buildscript {
    apply(from = "versions.gradle.kts")
    val kotlinVersion: String? by extra

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        maven {
            url = URI("https://dl.bintray.com/jps/maven")
        }
        google()
        mavenCentral()
    }
}