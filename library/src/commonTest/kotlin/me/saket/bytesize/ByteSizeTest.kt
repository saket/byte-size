package me.saket.bytesize

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.messageContains
import kotlin.test.Test
import me.saket.bytesize.ByteSize.Companion.BytesPerGb

class ByteSizeTest {

  @Test fun canary() {
    assertThat(ByteSize(bytes = 1_000).inWholeBytes).isEqualTo(1_000)
  }

  @Test fun max_value() {
    val max = ByteSize(Long.MAX_VALUE)
    assertThat(max.inWholeBytes).isEqualTo(Long.MAX_VALUE)
    assertThat(max.toString()).isEqualTo("9.2233718E9 GB")
  }

  @Test
  fun unit_conversions() {
    assertThat(2_000.bytes.inWholeKilobytes).isEqualTo(2)
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
    assertThat((6.gigabytes * 3.2f).toString()).isEqualTo(19.2.gigabytes.toString())  // Convert to string to ignore precision error.
    assertThat(6.gigabytes * 3.bytes).isEqualTo(18.gigabytes)
    assertThat(6.gigabytes * 3f.bytes).isEqualTo(18.gigabytes)
    assertThat(1.megabytes / 2).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toShort()).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toByte()).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.bytes).isEqualTo(500.kilobytes)
  }

  @Test
  fun trim_empty_decimals_from_toString() {
    assertThat(200.bytes.toString()).isEqualTo("200 bytes")
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

  @Test fun throw_an_error_if_multiplication_will_cause_an_overflow() {
    assertFailure {
      1_000_000_000_000_000L.bytes * 1e308
    }.hasMessage("Multiplication resulted in overflow")

    assertFailure {
      1_000_000_000L.bytes * Float.MAX_VALUE
    }.messageContains("Double value out of Long range")

    assertFailure {
      1_000_000_000L.bytes * Double.MAX_VALUE
    }.hasMessage("Multiplication resulted in overflow")
  }

  @Test fun throw_an_error_when_multiplication_is_performed_with_infinity() {
    assertFailure {
      1_000_000_000L.bytes * Float.POSITIVE_INFINITY
    }.hasMessage("Multiplication resulted in overflow")

    assertFailure {
      1_000_000_000L.bytes * Double.POSITIVE_INFINITY
    }.hasMessage("Multiplication resulted in overflow")
  }

  @Test fun throw_an_error_when_multiplication_is_performed_with_NaN() {
    assertFailure {
      1_000_000_000L.bytes * Float.NaN
    }.hasMessage("Cannot convert NaN to Long")

    assertFailure {
      1_000_000_000L.bytes * Double.NaN
    }.hasMessage("Cannot convert NaN to Long")
  }
}
