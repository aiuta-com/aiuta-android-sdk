import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl-base`
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.kotlin.compose)
    implementation(libs.gradlePlugin.vanniktech.publish)
}

// Target JVM 17.
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}
