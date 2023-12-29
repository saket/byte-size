plugins {
  id("java-library")
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.poko)
  alias(libs.plugins.metalava)
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.assertk)
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

metalava {
  filename.set("api/api.txt")
  enforceCheck.set(true)
  sourcePaths.setFrom("src/main") // Exclude tests.
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = properties["VERSION_NAME"] as String
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = $libraryVersion"
  }
}
