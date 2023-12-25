plugins {
  id("java-library")
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.mavenPublish)
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.truth)
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = properties["VERSION_NAME"] as String
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = $libraryVersion"
  }
}
