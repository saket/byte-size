package me.saket.bytesize

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import kotlin.test.Test

class BinaryByteSizeTest {

  @Test fun canary() {
    assertThat(BinaryByteSize(bytes = 1024).inWholeBytes).isEqualTo(1024)

    assertThat(1.kibibytes.inWholeBytes).isEqualTo(1024)
    assertThat(1.mebibytes.inWholeKibibytes).isEqualTo(1024)
    assertThat(1.gibibytes.inWholeMebibytes).isEqualTo(1024)
  }

  @Test fun max_value() {
    val max = BinaryByteSize(Long.MAX_VALUE)
    assertThat(max.inWholeBytes).isEqualTo(Long.MAX_VALUE)
    assertThat(max.toString()).isEqualTo("9.2233718E9 GiB")
  }

  @Test fun unit_conversions() {
    assertThat(2048.binaryBytes.inWholeKibibytes).isEqualTo(2)
    assertThat(345.999.kibibytes.inWholeKibibytes).isEqualTo(345)
    assertThat(3.2.gibibytes.inWholeMebibytes).isEqualTo(3276)
    assertThat(1.gibibytes.inWholeGibibytes).isEqualTo(1)
    assertThat(512.mebibytes.inWholeGibibytes).isEqualTo(0)
  }

  @Test fun basic_math_operations() {
    assertThat(6.mebibytes + 512.kibibytes).isEqualTo(6.5.mebibytes)
    assertThat(4.gibibytes - 700.mebibytes).isEqualTo(3_396.mebibytes)
    assertThat(7.gibibytes * 2.3).isEqualTo(16.1.gibibytes)
    assertThat(2.3 * 7.gibibytes).isEqualTo(16.1.gibibytes)
    assertThat((7.gibibytes * 2.3f).toString()).isEqualTo(16.1.gibibytes.toString())  // Convert to string to ignore precision error.
    assertThat(1.gibibytes / 2).isEqualTo(0.5.gibibytes)
    assertThat(1.gibibytes / 2).isEqualTo(512.mebibytes)
    assertThat(3.mebibytes / 2.toShort()).isEqualTo(1_536.kibibytes)
    assertThat(1.mebibytes / 2.binaryBytes).isEqualTo(524_288.0)
  }

  @Test fun format_to_string() {
    assertThat(200.binaryBytes.toString()).isEqualTo("200 bytes")
    assertThat(345.kibibytes.toString()).isEqualTo("345 KiB")
    assertThat(678.mebibytes.toString()).isEqualTo("678 MiB")
    assertThat(987.gibibytes.toString()).isEqualTo("987 GiB")
    assertThat(9_000.gibibytes.toString()).isEqualTo("9000 GiB")
  }

  @Test fun conversion_to_decimal_bytes() {
    assertThat(256.kibibytes.asDecimalBytes().inWholeKilobytes).isEqualTo(262)
    assertThat(400.mebibytes.asDecimalBytes().inWholeKilobytes).isEqualTo(419_430)
    assertThat(8.gibibytes.asDecimalBytes().inWholeMegabytes).isEqualTo(8_589)
  }

  @Test fun maths_with_decimal_bytes() {
    (2.gibibytes - 2.gigabytes).let {
      assertThat(it).isEqualTo(140.65136718750003.mebibytes)
      assertThat(it.toString()).isEqualTo("140.65 MiB")
      assertThat(it).isInstanceOf<BinaryByteSize>()
    }

    (3.mebibytes + 3.megabytes).let {
      assertThat(it).isEqualTo(5.86102294921875.mebibytes)
      assertThat(it.toString()).isEqualTo("5.86 MiB")
      assertThat(it).isInstanceOf<BinaryByteSize>()
    }
  }

  @Test fun comparison_with_decimal_bytes() {
    assertThat(1.gibibytes > 1.gigabytes).isTrue()
    assertThat(5.mebibytes < 5.megabytes).isFalse()
  }

  @Test fun throw_an_error_if_binaryBytes_is_used_for_a_fractional_number() {
    assertFailure {
      // Float#binaryBytes is a compilation error.
      // Bypass it by casting it to a generic number.
      (500.50f as Number).binaryBytes
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      (500.50 as Number).binaryBytes
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      BinaryByteSize(500.50f)
    }.hasMessage(PrecisionLossErrorMessage)

    assertFailure {
      BinaryByteSize(500.50)
    }.hasMessage(PrecisionLossErrorMessage)
  }
}
