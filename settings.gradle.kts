@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()  // For metalava.
  }
}

include(
  ":library",
)

rootProject.name = "file-size"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
