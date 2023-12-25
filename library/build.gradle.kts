plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.mavenPublish)
}

android {
  namespace = "me.saket.filesize"

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    compileSdk = libs.versions.compileSdk.get().toInt()
  }
  java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
  }
  lint {
    abortOnError = true
  }
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.truth)
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = properties["VERSION_NAME"] as String
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = $libraryVersion"
  }
}
