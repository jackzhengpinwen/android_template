plugins {
    `kotlin-dsl`
}



dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "android_template.android.application"
            implementationClass = "com.example.myapplication.AndroidApplicationConventionPlugin"
        }
    }
}