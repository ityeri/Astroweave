val gdxVersion: String by project
val kotlinVersion: String by project
val graalHelperVersion: String by project
val enableGraalNative: String by project

plugins {
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}



dependencies {
    api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    if (enableGraalNative == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:$graalHelperVersion")
    }

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation(project(":graph"))
}
