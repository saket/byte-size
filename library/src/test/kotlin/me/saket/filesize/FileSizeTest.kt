package me.saket.filesize

import com.google.common.truth.Truth.assertThat
import me.saket.filesize.FileSize.Companion.bytes
import me.saket.filesize.FileSize.Companion.gigabytes
import me.saket.filesize.FileSize.Companion.kilobytes
import me.saket.filesize.FileSize.Companion.megabytes
import org.junit.Test

class FileSizeTest {
  @Test fun `unit conversions`() {
    assertThat(2_000.bytes.inWholeKilobytes).isEqualTo(2)
    assertThat(3.2.gigabytes.inWholeMegabytes).isEqualTo(3_200)
    assertThat(1.gigabytes.inWholeGigabytes).isEqualTo(1)
    assertThat(512.megabytes.inWholeGigabytes).isEqualTo(0)
  }

  @Test fun `basic math operations`() {
    assertThat(3.megabytes + 200.kilobytes).isEqualTo(3.2.megabytes)
    assertThat(6.gigabytes * 3.2).isEqualTo(19.2.gigabytes)
    assertThat(6.gigabytes * 3.2.toFloat()).isEqualTo(19.2.gigabytes)
    assertThat(1.megabytes / 2).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toShort()).isEqualTo(500.kilobytes)
    assertThat(1.megabytes / 2.toByte()).isEqualTo(500.kilobytes)
    assertThat(7.gigabytes - 500.megabytes).isEqualTo(6_500.megabytes)
  }

  @Test fun `trim empty decimals from toString()`() {
    assertThat(200.bytes.toString()).isEqualTo("200 bytes")
    assertThat(345.kilobytes.toString()).isEqualTo("345 KB")
    assertThat(678.megabytes.toString()).isEqualTo("678 MB")
    assertThat(987.gigabytes.toString()).isEqualTo("987 GB")
    assertThat(9_000.gigabytes.toString()).isEqualTo("9000 GB")
  }

  @Test fun `round off decimals after 2 digits from toString()`() {
    assertThat(345.3.kilobytes.toString()).isEqualTo("345.3 KB")
    assertThat(512.255.megabytes.toString()).isEqualTo("512.26 MB")
    assertThat(345.999.kilobytes.toString()).isEqualTo("346 KB")
    assertThat(678.99999.gigabytes.toString()).isEqualTo("679 GB")
  }
}
