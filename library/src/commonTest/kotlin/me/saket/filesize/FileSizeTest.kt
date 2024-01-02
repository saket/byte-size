package me.saket.filesize

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import kotlin.test.Test
import me.saket.filesize.FileSize.Companion.bytes
import me.saket.filesize.FileSize.Companion.gigabytes
import me.saket.filesize.FileSize.Companion.kilobytes
import me.saket.filesize.FileSize.Companion.megabytes

class FileSizeTest {

  @Test
  fun unit_conversions() {
    assertThat(2_000.bytes.inWholeKilobytes).isEqualTo(2)
    assertThat(3.2.gigabytes.inWholeMegabytes).isEqualTo(3_200)
    assertThat(1.gigabytes.inWholeGigabytes).isEqualTo(1)
    assertThat(512.megabytes.inWholeGigabytes).isEqualTo(0)
  }

  @Test
  fun basic_math_operations() {
    assertThat(3.megabytes + 200.kilobytes).isEqualTo(3.2.megabytes)
    assertThat(7.gigabytes - 500.megabytes).isEqualTo(6_500.megabytes)
    assertThat(6.gigabytes * 3.2).isEqualTo(19.2.gigabytes)
    assertThat(6.gigabytes * 3.2f).isEqualTo(19.2.gigabytes)
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

//  @Test
//  fun throw_if_calculation_of_a_large_filesize_results_in_an_overflow() {
//    assertFailure { FileSize(bytes = Long.MAX_VALUE) * 2.0f }.hasMessage("Cannot convert to long and provide exact value")
//    assertFailure { FileSize(bytes = Long.MAX_VALUE) * 2.0 }.hasMessage("Cannot convert to long and provide exact value")
//
//    FileSize(bytes = Long.MAX_VALUE).let {
//      assertThat(it.inWholeBytes).isEqualTo(Long.MAX_VALUE)
//      assertFailure { it + 1.bytes }.hasMessage("Cannot convert to long and provide exact value")
//    }
//
//    assertFailure { 10_000_000_000.gigabytes }.hasMessage("Cannot convert to long and provide exact value")
//    assertFailure { 1_000_000_000_000.gigabytes }.hasMessage("Cannot convert to long and provide exact value")
//    assertFailure { 1_000_000.gigabytes * 1_000_000.gigabytes }.hasMessage("Cannot convert to long and provide exact value")
//  }
}
