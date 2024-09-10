package me.saket.bytesize

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.hasToString
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import kotlin.test.Test

class DecimalBitSizeTest {

  @Test fun canary() {
    assertThat(DecimalBitSize(bits = 1000).inWholeBits).isEqualTo(1000)

    assertThat(1.decimalBits.inWholeBits).isEqualTo(1)
    assertThat(8.decimalBits.inWholeBytes).isEqualTo(1)
    assertThat(1.kilobits.inWholeBytes).isEqualTo(125)
    assertThat(1.kilobits.inWholeBits).isEqualTo(1000)
    assertThat(1.megabits.inWholeKilobits).isEqualTo(1000)
    assertThat(1.gigabits.inWholeMegabits).isEqualTo(1000)
  }

  @Test fun max_value() {
    val max = DecimalBitSize(Long.MAX_VALUE)
    assertThat(max.inWholeBits).isEqualTo(Long.MAX_VALUE)
    assertThat(max).hasToString("9.2233718E9 Gb")
  }

  @Test fun unit_conversions() {
    assertThat(4000.decimalBits.inWholeKilobits).isEqualTo(4)
    assertThat(839.123.kilobits.inWholeKilobits).isEqualTo(839)
    assertThat(4.9.gigabits.inWholeMegabits).isEqualTo(4900)
    assertThat(1.gigabits.inWholeGigabits).isEqualTo(1)
    assertThat(500.megabits.inWholeGigabits).isEqualTo(0)
  }

  @Test fun basic_math_operations() {
    assertThat(7.decimalBits - 1.decimalBits).isEqualTo(6.decimalBits)
    assertThat(4.megabits + 300.kilobits).isEqualTo(4.3.megabits)
    assertThat(12.gigabits - 1500.megabits).isEqualTo(10_500.megabits)
    assertThat(7.gigabits * 3.3).isEqualTo(23.1.gigabits)
    assertThat(3.3 * 7.gigabits).isEqualTo(23.1.gigabits)
    assertThat((7.gigabits * 3.3f)).isApproximatelyEqualTo(23.1.gigabits)
    assertThat(1.megabits / 2).isEqualTo(500.kilobits)
    assertThat(1.megabits / 2.toShort()).isEqualTo(500.kilobits)
    assertThat(1.megabits / 2.toByte()).isEqualTo(500.kilobits)
    assertThat(1.megabits / 2.decimalBits).isEqualTo(500_000.0)
  }

  @Test fun format_to_string() {
    assertThat(4.decimalBits).hasToString("4 b")
    assertThat(430.decimalBits).hasToString("430 b")
    assertThat(672.kilobits).hasToString("672 Kb")
    assertThat(130.megabits).hasToString("130 Mb")
    assertThat(666.gigabits).hasToString("666 Gb")
    assertThat(123_456.gigabits).hasToString("123456 Gb")
  }

  @Test fun conversion_to_other_units() {
    assertThat(256.kilobits.asDecimalBytes()).isEqualTo(32.kilobytes)
    assertThat(1024.megabits.asDecimalBytes()).isEqualTo(128.megabytes)
    assertThat(8.gigabits.asDecimalBytes()).isEqualTo(1.gigabytes)

    assertThat(256.kilobits.asBinaryBytes()).isEqualTo(31.25.kibibytes)
    assertThat(1024.megabits.asBinaryBytes()).isApproximatelyEqualTo(122.07.mebibytes)
    assertThat(9.gigabits.asBinaryBytes()).isApproximatelyEqualTo(1.05.gibibytes)
  }

  @Test fun maths_with_other_units() {
    (7.decimalBits + 1.decimalBytes).let {
      assertThat(it).isEqualTo(15.decimalBits)
      assertThat(it).hasToString("15 b")
      assertThat(it).isInstanceOf<DecimalBitSize>()
    }

    (13.gigabits + 567.mebibytes).let {
      assertThat(it).isApproximatelyEqualTo(17.76.gigabits)
      assertThat(it).hasToString("17.76 Gb")
      assertThat(it).isInstanceOf<DecimalBitSize>()
    }
  }

  @Test fun comparison_with_other_units() {
    assertThat(1.kilobits < 1.kilobytes).isTrue()
    assertThat(8.01.gigabits > 1.gigabytes).isTrue()

    assertThat(1.kilobits > 1.kibibytes).isFalse()
    assertThat(8.6.gigabits > 1.gibibytes).isTrue()
  }

  @Test fun throw_an_error_if_binaryBits_is_used_for_a_fractional_number() {
    assertFailure {
      // Float#decimalBits is a compilation error.
      // Bypass it by casting it to a generic number.
      (123.45f as Number).decimalBits
    }.hasMessage(BitPrecisionLossErrorMessage)

    assertFailure {
      (123.45 as Number).decimalBits
    }.hasMessage(BitPrecisionLossErrorMessage)

    assertFailure {
      DecimalBitSize(123.45f)
    }.hasMessage(BitPrecisionLossErrorMessage)

    assertFailure {
      DecimalBitSize(123.45)
    }.hasMessage(BitPrecisionLossErrorMessage)
  }
}
