plugins {
    id("android_template.android.library")
    kotlin("kapt")
}

android {
    namespace = "com.example.core_testing"
}

dependencies {

    implementation(project(":core-common"))
//    implementation(project(":core-data"))
    implementation(project(":core-model"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    api(libs.junit4)
    api(libs.androidx.test.core)
    api(libs.kotlinx.coroutines.test)
}