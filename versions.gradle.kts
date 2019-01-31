mapOf(
        "kotlinVersion" to "1.3.20",
        "supportVersion" to "28.0.0",
        "compileVersion" to 28,
        "minVersion" to 19,
        "targetVersion" to 28,
        "appcompatVersion" to "1.1.0-alpha01",
        "buildVersion" to "28.0.3",
        "bintrayRepo" to "maven",
        "bintrayName" to "flipperlayout",
        "publishedGroupId" to "com.juniperphoton",
        "libraryName" to "flipperlayout",
        "artifact" to "flipperlayout",
        "libraryDescription" to "A view that performs perspective rotatation.",
        "siteUrl" to "https://github.com/JuniperPhoton/FlipperLayout",
        "gitUrl" to "https://github.com/JuniperPhoton/FlipperLayout.git",
        "libraryVersion" to "1.2.6",
        "developerId" to "juniperphoton",
        "developerName" to "JuniperPhoton",
        "developerEmail" to "dengweichao@hotmail.com",
        "licenseName" to "The Apache Software License, Version 2.0",
        "licenseUrl" to "http://www.apache.org/licenses/LICENSE-2.0.txt",
        "allLicenses" to arrayOf("Apache-2.0")
).forEach {
    project.extra.set(it.key, it.value)
}