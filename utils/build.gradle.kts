val gdxVersion: String by project

plugins {
}

group = "com.github.ityeri"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jsoup:jsoup:1.16.1")
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}
