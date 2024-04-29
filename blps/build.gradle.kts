plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.4"
//	id("com.intershop.gradle.jaxb") version "6.0.0"
}

group = "com.example"
version = "0.0.3"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.atomikos:transactions-spring-boot3-starter:6.0.0")
    implementation("org.postgresql:postgresql")
//    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.0")
//    implementation("com.sun.xml.bind:jaxb-impl:3.0.0")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
