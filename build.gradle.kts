import org.springframework.boot.gradle.tasks.aot.ProcessAot

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.7.2")
    }
}

plugins {
    id("java")
    id("idea")
    id("checkstyle")
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.7.2"
    id("org.graalvm.buildtools.native") version "0.11.0"
}

group = "dev.albertv.projects"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

flyway {
    url = flyway.url
    user = flyway.user
    password = flyway.password
}

checkstyle {
    toolVersion = "10.26.1"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring Addons
    implementation("com.c4-soft.springaddons:spring-addons-starter-oidc:8.1.18")

    // Azure
    implementation("com.azure.spring:spring-cloud-azure-starter-actuator")
    implementation("com.azure.spring:spring-cloud-azure-starter-storage")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql")

    // Flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    // JetBrains
    compileOnly("org.jetbrains:annotations:26.0.2")
}

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:5.22.0")
    }
}

tasks.withType<ProcessResources> {
    filesMatching("**/application*.yml") {
        expand(project.properties)
    }
}

graalvmNative {
    binaries {
        named("main") {
            imageName = "api"
            buildArgs.add("--initialize-at-build-time=org.slf4j.helpers.Reporter")
            buildArgs.add("-Djava.security.properties=${rootProject.projectDir}/src/main/resources/security.properties")
            buildArgs.add("-march=compatibility")
        }
    }
}

tasks.withType<ProcessAot> {
    args("--spring.profiles.active=prd")
}
