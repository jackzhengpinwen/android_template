plugins {
    id("android_template.android.library")
    kotlin("kapt")
}

android {
    namespace = "com.example.core_common"
}

kapt {
    generateStubs = true
}

dependencies {
    // appcompat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    // coroutine
    implementation(libs.kotlinx.coroutines.android)
    // lifecycle
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.livedata)
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // timber
    implementation(libs.timber)
    // okio
    implementation(libs.android.okio)
    // gson
    implementation(libs.retrofit.gson)
}