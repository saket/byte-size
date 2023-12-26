import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.mavenPublish) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.poko) apply false
  alias(libs.plugins.spotless) apply false
}

allprojects {
  plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
  extensions.configure<SpotlessExtension> {
    kotlin {
      target("src/**/*.kt")
      ktlint(rootProject.libs.ktlint.get().version).editorConfigOverride(
        mapOf(
          "ktlint_standard_filename" to "disabled",
        ),
      )
    }
    kotlinGradle {
      ktlint(rootProject.libs.ktlint.get().version)
    }
  }

  // Configure Java to use our chosen language level. Kotlin will automatically pick this up.
  // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
  plugins.withType<JavaBasePlugin>().configureEach {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion = JavaLanguageVersion.of(11)
      }
    }
  }
}
