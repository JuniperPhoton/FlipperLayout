val file = rootProject.file("local.properties")
val sb = StringBuilder()
file?.useLines { lines ->
    lines.forEach { lineString ->
        sb.append(lineString + "\n")

        val split = lineString.split('=')
        if (split.size != 2) return@forEach

        val name = split[0]
        val value = split[1]
        rootProject.extra[name] = value
    }
}

task("printLocal") {
    println("=====Local properties are: \n$sb")
}