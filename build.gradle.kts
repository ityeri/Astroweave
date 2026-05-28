plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.github.ityeri"

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

val ktorVersion = "3.5.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.logger)

    implementation("io.mola.galimatias:galimatias:0.2.1")

    implementation("io.github.oshai:kotlin-logging-jvm:8.0.03")
    implementation("io.klogging:klogging-jvm:0.7.3")
    implementation("io.klogging:slf4j-klogging:0.11.8")

    implementation("org.jsoup:jsoup:1.22.2")
}

kotlin {
    jvmToolchain(21)
}
