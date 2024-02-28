plugins {
	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
	id("org.openapi.generator") version "7.3.0"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.3.0")
	implementation("io.swagger.core.v3:swagger-models:2.2.20")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val openApiGenPort = 54433
val openApiOutputDir = "$buildDir/openapi"
val openApiClientOutputDir = "$openApiOutputDir/client"
val openApiOutputFileName = "openapi.json"

openApi {
	apiDocsUrl.set("http://localhost:${openApiGenPort}/v3/api-docs")
	outputDir.set(file(openApiOutputDir))
	outputFileName.set(openApiOutputFileName)
	customBootRun {
		args = listOf("--server.port=${openApiGenPort}", "--server.servlet.context-path=/")
	}
}

openApiGenerate {
	generatorName.set("java")
	library.set("native")
	apiPackage.set("eu.abelk.connectfive.api")
	modelPackage.set("eu.abelk.connectfive.api.model")
	inputSpec.set("$openApiOutputDir/$openApiOutputFileName")
	outputDir.set(openApiClientOutputDir)
	additionalProperties.set(mapOf("useJakartaEe" to true))
}

tasks.named("generateOpenApiDocs") {
	dependsOn("clean")
}

tasks.named("openApiGenerate") {
	dependsOn("generateOpenApiDocs")
}
