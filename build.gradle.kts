import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "com.project.university.specializedproject"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_19
}

jib {
    from {
        image = "eclipse-termurin:19.0.2_7-jre-jammy"
    }
    to {
        image = "docker.io/iamthanh/${project.name}"
    }
}
sourceSets {
    main {
        kotlin {
            srcDirs("src/main/kotlin")
            exclude("**/controller/**", "**/config/SecurityConfig.kt")
        }
    }
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "19"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}