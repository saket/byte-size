package me.saket.bytesize

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.Test

class ByteSizeTest {
  @Test fun absolute_value() {
    // Note to self: these values are intentionally typed as ByteSize to exercise the interface
    // extension (ByteSize#absoluteValue). Otherwise, they'll resolve to their implementation
    // extensions (e.g., BinaryByteSize#absoluteValue).
    val cases: List<Pair<ByteSize, ByteSize>> = listOf(
      (-5).kibibytes to 5.kibibytes,
      (-11).megabytes to 11.megabytes,
      (-63).gigabits to 63.gigabits,
    )
    for ((value, expected) in cases) {
      assertThat(value).isInstanceOf<ByteSize>()
      assertThat(value.absoluteValue).isEqualTo(expected)
    }
  }
}
