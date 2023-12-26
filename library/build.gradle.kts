plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.poko)
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.assertk)
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = providers.gradleProperty("VERSION_NAME").orNull.orEmpty()
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = $libraryVersion"
  }
}
