@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration
import kotlin.time.DurationUnit

/** Represents a byte or bit size per second. */
@JvmInline
value class Bitrate(
  @PublishedApi
  @get:JvmSynthetic
  internal val bytesPerSecond: ByteSize,
) : Comparable<Bitrate> {
  override inline operator fun compareTo(other: Bitrate): Int = bytesPerSecond.compareTo(other.bytesPerSecond)

  inline operator fun plus(other: Bitrate): Bitrate = Bitrate(bytesPerSecond + other.bytesPerSecond)

  inline operator fun minus(other: Bitrate): Bitrate = Bitrate(bytesPerSecond - other.bytesPerSecond)

  inline operator fun times(multiplier: Double): Bitrate = Bitrate(bytesPerSecond * multiplier)

  inline operator fun div(divisor: Double): Bitrate = Bitrate(bytesPerSecond / divisor)

  override inline fun toString(): String = "$bytesPerSecond/s"
}

@JvmSynthetic
inline operator fun ByteSize.div(duration: Duration): Bitrate {
  return Bitrate(this / duration.toDouble(DurationUnit.SECONDS))
}

@JvmSynthetic
inline operator fun Bitrate.times(duration: Duration): ByteSize {
  return bytesPerSecond * duration.toDouble(DurationUnit.SECONDS)
}

@JvmSynthetic
inline operator fun Duration.times(bitrate: Bitrate): ByteSize {
  return bitrate * this
}
