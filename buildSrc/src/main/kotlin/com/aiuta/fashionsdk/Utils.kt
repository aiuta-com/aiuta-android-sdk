package com.aiuta.fashionsdk

import kotlin.math.pow
import org.gradle.api.Project

// SDK
val Project.minSdk: Int
    get() = intProperty("minSdk")

val Project.targetSdk: Int
    get() = intProperty("targetSdk")

val Project.compileSdk: Int
    get() = intProperty("compileSdk")

// ID
val Project.groupId: String
    get() = stringProperty("POM_GROUP_ID")

val Project.artifactId: String
    get() = stringProperty("POM_ARTIFACT_ID")

// POM
val Project.pomName: String
    get() = stringProperty("POM_NAME")

val Project.pomDescription: String
    get() = stringProperty("POM_DESCRIPTION")

val Project.pomUrl: String
    get() = stringProperty("POM_URL")

val Project.pomUrlIssue: String
    get() = stringProperty("POM_URL_ISSUE")

// SCM
val Project.scmUrl: String
    get() = stringProperty("POM_SCM_URL")

val Project.scmConnection: String
    get() = stringProperty("POM_SCM_CONNECTION")

val Project.scmDevConnection: String
    get() = stringProperty("POM_SCM_DEV_CONNECTION")

// License
val Project.licenceName: String
    get() = stringProperty("POM_LICENCE_NAME")

val Project.licenceUrl: String
    get() = stringProperty("POM_LICENCE_URL")

val Project.licenceDist: String
    get() = stringProperty("POM_LICENCE_DIST")

// Version
val Project.versionName: String
    get() = stringProperty("POM_VERSION")

val Project.versionCode: Int
    get() =
        versionName
            .takeWhile { it.isDigit() || it == '.' }
            .split('.')
            .map { it.toInt() }
            .reversed()
            .sumByIndexed { index, unit ->
                // 3.2.1 -> 302010
                (unit * 10.0.pow(2 * index + 1)).toInt()
            }

private fun Project.intProperty(
    name: String,
    default: () -> Int = { error("unknown property: $name") },
): Int = (properties[name] as String?)?.toInt() ?: default()

private fun Project.stringProperty(
    name: String,
    default: () -> String = { error("unknown property: $name") },
): String = (properties[name] as String?) ?: default()

private inline fun <T> List<T>.sumByIndexed(selector: (Int, T) -> Int): Int {
    var index = 0
    var sum = 0
    for (element in this) {
        sum += selector(index++, element)
    }
    return sum
}
