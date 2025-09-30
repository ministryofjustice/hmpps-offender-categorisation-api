plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "9.0.0"
  kotlin("plugin.spring") version "2.2.10"
  kotlin("plugin.jpa") version "2.1.20"
  kotlin("plugin.serialization") version "1.9.25"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

// https://mojdt.slack.com/archives/C69NWE339/p1734943189790819
ext["logback.version"] = "1.5.14"

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")
  runtimeOnly("org.postgresql:postgresql:42.7.7")

  // Spring boot dependencies
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("io.opentelemetry:opentelemetry-api:1.34.1")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.14.0")
  implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.7.3")

  implementation("com.vladmihalcea:hibernate-types-60:2.21.1")
  implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.5.1")

  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.4.2")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.2.2")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

  // OpenAPI
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.11")

  implementation("com.opencsv:opencsv:5.9")
  implementation("aws.sdk.kotlin:s3:1.4.111")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
  implementation("io.opentelemetry:opentelemetry-extension-kotlin:1.51.0")

  // Test dependencies
  testImplementation("com.h2database:h2:2.3.232")
  testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:3.0.1")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.1")
  testImplementation("io.jsonwebtoken:jjwt-api:0.13.0")
  testImplementation("io.jsonwebtoken:jjwt-impl:0.13.0")
  testImplementation("io.jsonwebtoken:jjwt-orgjson:0.13.0")
  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
  testImplementation("io.swagger.parser.v3:swagger-parser-v2-converter:2.1.26")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("com.h2database:h2")
  testImplementation("org.testcontainers:postgresql:1.19.7")
  testImplementation("org.testcontainers:localstack:1.19.7")
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}
