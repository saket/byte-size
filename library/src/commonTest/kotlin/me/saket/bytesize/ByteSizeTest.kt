package me.saket.bytesize

import assertk.assertFailure
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

  @Test fun int_and_long_receivers_agree_with_number_receivers() {
    // The Int and Long overloads of these properties exist only to keep the receiver unboxed, so
    // they must produce exactly what the Number overload they shadow would have produced.
    // See AllocationFreeCallSitesTest.
    for (n in listOf(0, 1, -1, 7, 345, 1_000_000, -1_636_186_211)) {
      val number: Number = n
      val long: Long = n.toLong()

      assertThat(n.decimalBytes).isEqualTo(number.decimalBytes)
      assertThat(n.kilobytes).isEqualTo(number.kilobytes)
      assertThat(n.megabytes).isEqualTo(number.megabytes)
      assertThat(n.gigabytes).isEqualTo(number.gigabytes)

      assertThat(n.binaryBytes).isEqualTo(number.binaryBytes)
      assertThat(n.kibibytes).isEqualTo(number.kibibytes)
      assertThat(n.mebibytes).isEqualTo(number.mebibytes)
      assertThat(n.gibibytes).isEqualTo(number.gibibytes)

      assertThat(n.decimalBits).isEqualTo(number.decimalBits)
      assertThat(n.kilobits).isEqualTo(number.kilobits)
      assertThat(n.megabits).isEqualTo(number.megabits)
      assertThat(n.gigabits).isEqualTo(number.gigabits)

      assertThat(long.decimalBytes).isEqualTo(number.decimalBytes)
      assertThat(long.kilobytes).isEqualTo(number.kilobytes)
      assertThat(long.megabytes).isEqualTo(number.megabytes)
      assertThat(long.gigabytes).isEqualTo(number.gigabytes)

      assertThat(long.binaryBytes).isEqualTo(number.binaryBytes)
      assertThat(long.kibibytes).isEqualTo(number.kibibytes)
      assertThat(long.mebibytes).isEqualTo(number.mebibytes)
      assertThat(long.gibibytes).isEqualTo(number.gibibytes)

      assertThat(long.decimalBits).isEqualTo(number.decimalBits)
      assertThat(long.kilobits).isEqualTo(number.kilobits)
      assertThat(long.megabits).isEqualTo(number.megabits)
      assertThat(long.gigabits).isEqualTo(number.gigabits)
    }
  }

  @Test fun int_and_long_receivers_reject_overflowing_unit_multiples() {
    assertFailure { Long.MAX_VALUE.kilobytes }.isInstanceOf<ArithmeticException>()
    assertFailure { Long.MAX_VALUE.gibibytes }.isInstanceOf<ArithmeticException>()
    assertFailure { Long.MIN_VALUE.megabits }.isInstanceOf<ArithmeticException>()
  }
}
