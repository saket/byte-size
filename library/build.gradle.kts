plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.poko)
  alias(libs.plugins.metalava)
}

kotlin {
  applyDefaultHierarchyTemplate()
  jvm()
  iosX64()
  iosSimulatorArm64()
  iosArm64()
  macosArm64()
  macosX64()
  linuxX64()
  linuxArm64()
  mingwX64()
  js(IR) {
    useCommonJs()
    browser {
      testTask {
        useKarma {
          useChromeHeadless()
        }
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        api(libs.bignum)
      }
    }
    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.assertk)
      }
    }
  }
}

metalava {
  filename.set("api/api.txt")
  enforceCheck.set(true)
  sourcePaths.setFrom("src/commonMain") // Exclude tests.
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = properties["VERSION_NAME"] as String
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = '$libraryVersion'"
  }
}
