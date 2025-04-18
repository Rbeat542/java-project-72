import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("org.sonarqube") version "6.0.1.5171"
    id("io.freefair.lombok") version "8.13.1"
    application
    checkstyle
    jacoco
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("hexlet.code.App")
}

sonar {
    properties {
        property("sonar.projectKey", "Rbeat542_java-project-72")
        property("sonar.organization", "rbeat542")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

dependencies {
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.1.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("gg.jte:jte:3.1.9")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.apache.commons:commons-collections4:4.4")
}

tasks.test {
    useJUnitPlatform()
    //finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}

