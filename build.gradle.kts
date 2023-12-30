plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.mavenPublish) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.poko) apply false
  alias(libs.plugins.metalava) apply false
}

allprojects {
  // Configure Java to use our chosen language level. Kotlin will automatically pick this up.
  // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
  plugins.withType<JavaBasePlugin>().configureEach {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion = JavaLanguageVersion.of(8)
      }
    }
  }
}
