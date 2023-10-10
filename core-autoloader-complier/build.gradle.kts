plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(project(":core-autoloader-annotation"))
    annotationProcessor(libs.auto.service)
    implementation(libs.auto.service.annotations)
    implementation(libs.javapoet)
}