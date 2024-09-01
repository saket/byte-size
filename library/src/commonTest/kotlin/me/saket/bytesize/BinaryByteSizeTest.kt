package me.saket.bytesize

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class BinaryByteSizeTest {

  @Test fun canary() {
    assertThat(BinaryByteSize(bytes = 1024).inWholeBytes).isEqualTo(1024)
    assertThat(1.mebibytes.inWholeBytes).isEqualTo(1024)
  }

  @Test fun foo() {
    val perception = 2.gibibytes
    val usable = 2.gigabytes
    val lost = perception - usable
    println("$lost lost on a 2GB drive.")   // = 147.48 MB lost on a 2GB drive.
  }
}
