pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include(
  ":library",
)

rootProject.name = "file-size"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
