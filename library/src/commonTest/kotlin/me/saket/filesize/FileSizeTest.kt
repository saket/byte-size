package me.saket.filesize

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import me.saket.filesize.FileSize.Companion.bytes
import me.saket.filesize.FileSize.Companion.gigabytes
import me.saket.filesize.FileSize.Companion.kilobytes
import me.saket.filesize.FileSize.Companion.megabytes

class FileSizeTest {

  @Test
  fun unitConversions() {
    assertEquals(expected = 2, actual = 2_000.bytes.inWholeKilobytes)
    assertEquals(expected = 3_200, actual = 3.2.gigabytes.inWholeMegabytes)
    assertEquals(expected = 1, actual = 1.gigabytes.inWholeGigabytes)
    assertEquals(expected = 0, actual = 512.megabytes.inWholeGigabytes)
  }

  @Test
  fun basicMathOperations() {
    assertEquals(expected = 3.2.megabytes, actual = 3.megabytes + 200.kilobytes)
    assertEquals(expected = 19.2.gigabytes, actual = 6.gigabytes * 3.2)
    assertEquals(expected = 19.2.gigabytes, actual = 6.gigabytes * 3.2f)
    assertEquals(expected = 500.kilobytes, actual = 1.megabytes / 2)
    assertEquals(expected = 500.kilobytes, actual = 1.megabytes / 2.toShort())
    assertEquals(expected = 500.kilobytes, actual = 1.megabytes / 2.toByte())
    assertEquals(expected = 6_500.megabytes, actual = 7.gigabytes - 500.megabytes)
  }

  @Test
  fun trimEmptyDecimalsFromString() {
    assertEquals(expected = "200 bytes", actual = 200.bytes.toString())
    assertEquals(expected = "345 KB", actual = 345.kilobytes.toString())
    assertEquals(expected = "678 MB", actual = 678.megabytes.toString())
    assertEquals(expected = "987 GB", actual = 987.gigabytes.toString())
    assertEquals(expected = "9000 GB", actual = 9_000.gigabytes.toString())
  }

  @Test
  fun roundAfter2DigitsFromString() {
    assertEquals(expected = "345.3 KB", actual = 345.3.kilobytes.toString())
    assertEquals(expected = "512.26 MB", actual = 512.255.megabytes.toString())
    assertEquals(expected = "346 KB", actual = 345.999.kilobytes.toString())
    assertEquals(expected = "679 GB", actual = 678.99999.gigabytes.toString())
  }

  @Test
  fun throwIfOverflow() {
    assertFailsWith<ArithmeticException> { FileSize(bytes = Long.MAX_VALUE) * 2.0f }
    assertFailsWith<ArithmeticException> { FileSize(bytes = Long.MAX_VALUE) * 2.0 }

    FileSize(bytes = Long.MAX_VALUE).let {
      assertEquals(expected = Long.MAX_VALUE, actual = it.inWholeBytes)
      assertFailsWith<ArithmeticException> { it + 1.bytes }
    }

    assertFailsWith<ArithmeticException> { 10_000_000_000.gigabytes }
    assertFailsWith<ArithmeticException> { 1_000_000_000_000.gigabytes }
    assertFailsWith<ArithmeticException> { 1_000_000.gigabytes * 1_000_000.gigabytes }
  }

  @Test
  fun throwIfDoubleConverted() {
    val number = 2.4

    assertFailsWith<FileSizePrecisionException> {
      (number as Number).bytes
    }
  }
}
