package com.example.myapplication

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.example.myapplication.internal.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure(BaseAppModuleExtension::class.java) {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33
            }
        }
    }
}