import java.net.URI

buildscript {
    apply(from = "versions.gradle.kts")
    val kotlinVersion: String? by extra

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        classpath("com.github.dcendents:android-maven-gradle-plugin:1.5")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.6")
    }
}

allprojects {
    repositories {
        maven {
            url = URI("https://dl.bintray.com/jps/maven")
        }
        jcenter()
        google()
    }
}