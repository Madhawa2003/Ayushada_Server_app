plugins {
    java
    id("org.springframework.boot") version "3.3.4" // Stable Spring Boot 3 version for Java 17
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.sliit"
version = "0.0.1-SNAPSHOT"
description = "Ayushada_Server"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
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
    // Web & REST API
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database & Persistence (includes JDBC & HikariCP automatically)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Lombok (Clean boilerplate reduction)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing (spring-boot-starter-test covers JUnit, Mockito, Spring Test, AssertJ)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}