package me.saket.bytesize

import assertk.assertThat
import assertk.assertions.hasToString
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class BitrateTest {
  @Test fun plus() {
    val rate1 = 10.megabytes / 1.seconds
    val rate2 = 5.megabytes / 1.seconds
    assertThat(rate1 + rate2).isEqualTo(15.megabytes / 1.seconds)
  }

  @Test fun minus() {
    val rate1 = 10.megabytes / 1.seconds
    val rate2 = 5.megabytes / 1.seconds
    assertThat(rate1 - rate2).isEqualTo(5.megabytes / 1.seconds)
  }

  @Test fun times_double() {
    val rate = 10.megabytes / 1.seconds
    assertThat(rate * 2.5).isEqualTo(25.megabytes / 1.seconds)
  }

  @Test fun div_double() {
    val rate = 10.megabytes / 1.seconds
    assertThat(rate / 2.5).isEqualTo(4.megabytes / 1.seconds)
  }

  @Test fun binary_byte_size_div_duration() {
    val rate = 100.mebibytes / 3.2.seconds
    assertThat(rate).hasToString("31.25 MiB/s")
  }

  @Test fun decimal_byte_size_div_duration() {
    val rate = 100.megabytes / 4.seconds
    assertThat(rate).hasToString("25 MB/s")
  }

  @Test fun decimal_bit_size_div_duration() {
    val rate = 100.megabits / 2.seconds
    assertThat(rate).hasToString("50 Mb/s")
  }

  @Test fun bitrate_times_duration() {
    val rate = 10.megabytes / 1.seconds
    val size = rate * 5.seconds
    assertThat(size).isEqualTo(50.megabytes)
  }

  @Test fun duration_times_bitrate() {
    val rate = 10.megabytes / 1.seconds
    val size = 5.seconds * rate
    assertThat(size).isEqualTo(50.megabytes)
  }

  @Test fun bitrate_times_duration_with_minutes() {
    val rate = 1.megabytes / 1.seconds
    val size = rate * 1.minutes
    assertThat(size).isEqualTo(60.megabytes)
  }

  @Test fun comparison() {
    val slow = 10.megabytes / 1.seconds
    val fast = 100.megabytes / 1.seconds
    assertThat(slow).isLessThan(fast)
    assertThat(fast).isGreaterThan(slow)
  }

  @Test fun comparison_across_types() {
    val binary = 1.gibibytes / 1.seconds
    val decimal = 1.gigabytes / 1.seconds
    assertThat(binary).isGreaterThan(decimal)
  }
}
