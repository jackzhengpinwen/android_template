pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "android_template"
include(":app")
include(":core-common")
include(":core-datastore")
include(":core-model")
include(":core-testing")
include(":core-datastore-test")
include(":core-database")
include(":core-network")
include(":sync")
include(":core-data")
