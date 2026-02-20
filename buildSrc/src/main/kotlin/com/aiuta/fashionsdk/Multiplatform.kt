package com.aiuta.fashionsdk

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun Project.addAllMultiplatformTargets(
    enableExtendedTargets: Boolean = true,
) {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            applyAiutaHierarchyTemplate()

            jvm()

            iosArm64()
            iosSimulatorArm64()

            if (enableExtendedTargets) {
                js {
                    browser()
                    nodejs {
                        testTask {
                            useMocha {
                                timeout = "60s"
                            }
                        }
                    }
                    binaries.executable()
                    binaries.library()
                }

                @OptIn(ExperimentalWasmDsl::class)
                wasmJs {
                    browser {
                        testTask {
                            enabled = false
                        }
                    }
                    nodejs {
                        testTask {
                            enabled = false
                        }
                    }
                    binaries.executable()
                    binaries.library()
                }
            }
        }
    }
}

val NamedDomainObjectContainer<KotlinSourceSet>.desktopMain: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named("desktopMain")

val NamedDomainObjectContainer<KotlinSourceSet>.mobileMain: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named("mobileMain")
