plugins {
    kotlin("jvm") version "2.1.20-Beta1"
}

group = "com.github.ityeri"

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
//    maven("https://s01.oss.sonatype.org")
//    maven('https://oss.sonatype.org/content/repositories/snapshots/')
//    maven('https://s01.oss.sonatype.org/content/repositories/snapshots/')
}

val ktorVersion = "3.5.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}

kotlin {
    jvmToolchain(21)
}
