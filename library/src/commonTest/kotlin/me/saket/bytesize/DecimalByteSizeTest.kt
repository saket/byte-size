package me.saket.bytesize

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import assertk.assertions.messageContains
import kotlin.test.Test

class DecimalByteSizeTest {

  @Test fun canary() {
    assertThat(DecimalByteSize(bytes = 1_000).inWholeBytes).isEqualTo(1_000)

    assertThat(1.kilobytes.inWholeBytes).isEqualTo(1_000)
    assertThat(1.megabytes.inWholeKilobytes).isEqualTo(1_000)
    assertThat(1.gigabytes.inWholeMegabytes).isEqualTo(1_000)
  }

  @Test fun max_value() {
    val max = DecimalByteSize(Long.MAX_VALUE)
    assertThat(max.inWholeBytes).isEqualTo(Long.MAX_VALUE)
    assertThat(max.toString()).isEqualTo("9.2233718E9 GB")
  }

  @Test
  fun unit_conversions() {
    assertThat(2_000.decimalBytes.inWholeKilobytes).isEqualTo(2)
    assertThat(345.999.kilobytes.inWholeKilobytes).isEqualTo(345)
    assertThat(3.2.gigabytes.inWholeMegabytes).isEqualTo(3_200)
    assertThat(1.gigabytes.inWholeGigabytes).isEqualTo(1)
    assertThat(512.megabytes.inWholeGigabytes).isEqualTo(0)
  }

  @Test
  fun basic_math_operations() {
    assertThat(3.megabytes + 200.kilobytes).isEqualTo(3.2.megabytes)
    assertThat(7.gigabytes - 500.megabytes).isEqualTo(6_500.megabytes)
    assertThat(6.gigabytes * 3.2).isEqualTo(19.2.gigabytes)
    assertThat(3.2 * 6.gigabytes).isEqualTo(19.2.gigabytes)
    assertThat((6.gigabytes * 3.2f).toString()).isEqualTo(19.2.gigabytes.toString())  // Convert to string to ignore precision error.
    assertThat(1.megabytes / 2).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toShort()).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toByte()).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.decimalBytes).isEqualTo(500_000.0)
  }

  @Test
  fun trim_empty_decimals_from_toString() {
    assertThat(200.decimalBytes.toString()).isEqualTo("200 bytes")
    assertThat(345.kilobytes.toString()).isEqualTo("345 KB")
    assertThat(678.megabytes.toString()).isEqualTo("678 MB")
    assertThat(987.gigabytes.toString()).isEqualTo("987 GB")
    assertThat(9_000.gigabytes.toString()).isEqualTo("9000 GB")
  }

  @Test
  fun round_off_decimals_after_2_digits_from_toString() {
    assertThat(345.3.kilobytes.toString()).isEqualTo("345.3 KB")
    assertThat(512.255.megabytes.toString()).isEqualTo("512.26 MB")
    assertThat(345.999.kilobytes.toString()).isEqualTo("346 KB")
    assertThat(678.99999.gigabytes.toString()).isEqualTo("679 GB")
  }

  @Test fun conversion_to_binary_bytes() {
    assertThat(700.kilobytes.asBinaryBytes().inWholeKibibytes).isEqualTo(683)
    assertThat(512.megabytes.asBinaryBytes().inWholeKibibytes).isEqualTo(500_000)
    assertThat(9.gigabytes.asBinaryBytes().inWholeMebibytes).isEqualTo(8_583)
  }

  @Test fun maths_with_binary_bytes() {
    (10.gigabytes - 3.gibibytes).let {
      assertThat(it).isEqualTo(6.778774528.gigabytes)
      assertThat(it.toString()).isEqualTo("6.78 GB")
      assertThat(it).isInstanceOf<DecimalByteSize>()
    }

    (5.megabytes + 500.kibibytes).let {
      assertThat(it).isEqualTo(5.512.megabytes)
      assertThat(it.toString()).isEqualTo("5.51 MB")
      assertThat(it).isInstanceOf<DecimalByteSize>()
    }
  }

  @Test fun comparison_with_binary_bytes() {
    assertThat(1.gigabytes < 1.gibibytes).isTrue()
    assertThat(5.megabytes < 1.mebibytes).isFalse()
  }

  @Test fun throw_an_error_if_multiplication_will_cause_an_overflow() {
    assertFailure {
      1_000_000_000_000_000L.decimalBytes * 1e308
    }.hasMessage("Multiplication resulted in overflow")

    assertFailure {
      1_000_000_000L.decimalBytes * Float.MAX_VALUE
    }.messageContains("Double value out of Long range")

    assertFailure {
      1_000_000_000L.decimalBytes * Double.MAX_VALUE
    }.hasMessage("Multiplication resulted in overflow")
  }

  @Test fun throw_an_error_when_multiplication_is_performed_with_infinity() {
    assertFailure {
      1_000_000_000L.decimalBytes * Float.POSITIVE_INFINITY
    }.hasMessage("Multiplication resulted in overflow")

    assertFailure {
      1_000_000_000L.decimalBytes * Double.POSITIVE_INFINITY
    }.hasMessage("Multiplication resulted in overflow")
  }

  @Test fun throw_an_error_when_multiplication_is_performed_with_NaN() {
    assertFailure {
      1_000_000_000L.decimalBytes * Float.NaN
    }.hasMessage("Cannot convert NaN to Long")

    assertFailure {
      1_000_000_000L.decimalBytes * Double.NaN
    }.hasMessage("Cannot convert NaN to Long")
  }

  @Test fun throw_an_error_if_decimalBytes_is_used_for_a_fractional_number() {
    assertFailure {
      // Float#decimalBytes is a compilation error.
      // Bypass it by casting it to a generic number.
      (500.50f as Number).decimalBytes
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      (500.50 as Number).decimalBytes
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      DecimalByteSize(500.50f)
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      DecimalByteSize(500.50)
    }.hasMessage(PrecisionLossErrorMessage)
  }
}
