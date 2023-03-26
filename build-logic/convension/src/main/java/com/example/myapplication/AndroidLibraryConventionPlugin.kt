package com.example.myapplication

import com.android.build.gradle.LibraryExtension
import com.example.myapplication.internal.configureFlavors
import com.example.myapplication.internal.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure(LibraryExtension::class.java) {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33
                defaultConfig.multiDexEnabled = true
                viewBinding.enable = true
                dataBinding.enable = true
                configureFlavors(this)
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                configurations.configureEach {
                    resolutionStrategy {
                        force(libs.findLibrary("junit4").get())
                        // Temporary workaround for https://issuetracker.google.com/174733673
                        force("org.objenesis:objenesis:2.6")
                    }
                }
            }
        }
    }
}