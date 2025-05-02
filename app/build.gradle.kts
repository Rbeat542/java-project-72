import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("org.sonarqube") version "6.0.1.5171"
    id("io.freefair.lombok") version "8.13.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("io.javalin:javalin:6.6.0")
    implementation("io.javalin:javalin-testtools:6.6.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("gg.jte:jte:3.2.1")
    implementation ("org.jsoup:jsoup:1.18.1")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.konghq:unirest-java-bom:4.4.6")
    implementation("com.konghq:unirest-java-core:4.4.6")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
        html.required = true
    }
}

checkstyle {
    toolVersion = "10.12.4"
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}