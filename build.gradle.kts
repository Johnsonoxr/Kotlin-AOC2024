plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.johnsonoxr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.johnsonoxr:exnumber:v1.0.5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}