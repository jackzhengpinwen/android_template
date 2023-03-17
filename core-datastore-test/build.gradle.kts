plugins {
    id("android_template.android.library")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.core_datastore_test"
}

dependencies {
    api(project(":core-datastore"))
    implementation(project(":core-testing"))

    api(libs.androidx.dataStore.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)
}