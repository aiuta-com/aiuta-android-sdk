@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.aiuta.fashionsdk

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        groupNonAndroid()
        groupJsCommon()
        groupNonJsCommon()
        groupJvmCommon()
        groupNative()
        groupMobile()
    }
}

private fun KotlinHierarchyBuilder.groupNonAndroid() {
    group("nonAndroid") {
        withJvm()
        groupJsCommon()
        groupNative()
    }
}

private fun KotlinHierarchyBuilder.groupJsCommon() {
    group("jsCommon") {
        withJs()
        withWasmJs()
    }
}

private fun KotlinHierarchyBuilder.groupNonJsCommon() {
    group("nonJsCommon") {
        groupJvmCommon()
        groupNative()
    }
}

private fun KotlinHierarchyBuilder.groupJvmCommon() {
    group("jvmCommon") {
        withCompilations { compilation ->
            compilation.target.platformType.name == "androidJvm" ||
                compilation.target.platformType.name == "jvm"
        }
    }
}

private fun KotlinHierarchyBuilder.groupNative() {
    group("native") {
        withNative()

        group("apple") {
            withApple()

            group("ios") {
                withIos()
            }

            group("macos") {
                withMacos()
            }
        }
    }
}

private fun KotlinHierarchyBuilder.groupMobile() {
    group("mobile") {
        withCompilations { compilation ->
            val platformType = compilation.target.platformType.name
            val targetName = compilation.target.name
            platformType == "androidJvm" || targetName.startsWith("ios")
        }
    }
}

fun KotlinMultiplatformExtension.applyAiutaHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}
