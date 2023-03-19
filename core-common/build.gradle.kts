plugins {
    id("android_template.android.library")
    kotlin("kapt")
}

android {
    namespace = "com.example.core_common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)
}