plugins {
    application
}

dependencies {
    implementation(project(":common"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson.core:jackson-databind")
}

application {
    mainClass = "eu.abelk.connectfive.client.ConnectFiveClientApplication"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
