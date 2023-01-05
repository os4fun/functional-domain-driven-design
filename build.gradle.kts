import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.spring") version "1.7.21"
}

object Versions {
    const val JUNIT = "5.9.0"
    const val MOCKK = "1.13.3"
    const val ASSERTJ = "3.20.2"
    const val ARROW = "1.1.2"
    const val FAKER = "1.0.2"
    const val APACHE_COMMONS_LANG = "3.12.0"
    const val JACKSON_KOTLIN = "2.14.0"
    const val SNAKEYAML = "1.33"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    google()
    mavenCentral()
}

extra["springCloudGcpVersion"] = "3.4.0"
extra["springCloudVersion"] = "2021.0.5"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation(platform("io.arrow-kt:arrow-stack:${Versions.ARROW}"))
    implementation("io.arrow-kt:arrow-core")

    implementation(group = "org.apache.commons", name ="commons-lang3", version = Versions.APACHE_COMMONS_LANG)
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", Versions.JACKSON_KOTLIN)

    implementation("com.google.cloud:spring-cloud-gcp-starter")
    implementation("com.google.cloud:spring-cloud-gcp-starter-pubsub")
    implementation("com.google.cloud:spring-cloud-gcp-starter-storage")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation(group = "org.yaml", name = "snakeyaml", version = Versions.SNAKEYAML)
    testImplementation(group = "com.github.javafaker", name = "javafaker", version = Versions.FAKER)
    testImplementation(group = "io.mockk", name = "mockk", version = Versions.MOCKK)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = Versions.JUNIT)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = Versions.JUNIT)
    testImplementation(group = "org.assertj", name = "assertj-core", version = Versions.ASSERTJ)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${property("springCloudGcpVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xinline-classes")
    }
}

tasks.apply {
    test {
        enableAssertions = true
        useJUnitPlatform {}
    }

    task<Test>("unitTest") {
        description = "Runs unit tests."
        useJUnitPlatform {
            excludeTags("integration")
            excludeTags("component")
        }
        shouldRunAfter(test)
    }

    task<Test>("integrationTest") {
        description = "Runs integration tests."
        useJUnitPlatform {
            includeTags("integration")
        }
        shouldRunAfter(test)
    }

    task<Test>("componentTest") {
        description = "Runs component tests."
        useJUnitPlatform {
            includeTags("component")
        }
        shouldRunAfter(test)
    }
}
