import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

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

  tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }
}
