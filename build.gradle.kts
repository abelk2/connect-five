plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    group = "eu.abelk.connectfive"
    version = "0.0.1-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
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
        testImplementation("org.testng:testng:7.9.0")
        testImplementation("org.mockito:mockito-testng:0.5.2")
        testImplementation("org.mockito:mockito-core")
        compileOnly("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
    }

    tasks.withType<Test> {
        useTestNG()
        testLogging {
            events("passed", "failed", "skipped")
        }
        finalizedBy(tasks["jacocoTestReport"])
    }

    tasks.withType<JacocoReport> {
        dependsOn(tasks["test"])
    }
}
