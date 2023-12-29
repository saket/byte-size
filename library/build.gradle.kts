plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.poko)
  alias(libs.plugins.metalava)
}

kotlin {
  jvm()

  iosX64()
  iosSimulatorArm64()
  iosArm64()
  macosArm64()
  macosX64()

  js(IR) {
    useEsModules()
    browser {
      testTask {
        useKarma {
          useChromeHeadless()
        }
      }
    }
  }

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }
    val nonJsMain by creating {
      dependsOn(commonMain.get())
    }

    jsMain {
      dependencies {
        implementation(npm("js-big-decimal", "2.0.4"))
      }
    }
    jvmMain {
      dependsOn(nonJsMain)
    }
    appleMain {
      dependsOn(nonJsMain)
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
