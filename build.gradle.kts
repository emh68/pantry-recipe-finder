plugins {
    kotlin("jvm") version "2.3.0"
}

group = "io.github.emh68"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.json:json:20240303")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}