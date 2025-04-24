plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "com.github.ityeri"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(project(":utils"))
}

kotlin {
    jvmToolchain(20)
}
