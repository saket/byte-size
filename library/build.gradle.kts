plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.metalava)
}

kotlin {
  applyDefaultHierarchyTemplate()

  androidNativeArm32()
  androidNativeArm64()
  androidNativeX86()
  androidNativeX64()

  iosArm64()
  iosSimulatorArm64()
  iosX64()

  js {
    useCommonJs()
    browser {
      testTask {
        useKarma {
          useChromeHeadless()
        }
      }
    }
  }

  jvm()

  linuxArm64()
  linuxX64()

  macosArm64()
  macosX64()

  mingwX64()

  tvosArm64()
  tvosSimulatorArm64()
  tvosX64()

  watchosArm64()
  watchosDeviceArm64()
  watchosSimulatorArm64()
  watchosX64()

  wasmJs().nodejs()
  wasmWasi().nodejs()

  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kmpMath)
      }
    }
    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.assertk)
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.kotlin.reflect)
      }
    }
  }
}

metalava {
  filename.set("api/api.txt")
  enforceCheck.set(true)
}
