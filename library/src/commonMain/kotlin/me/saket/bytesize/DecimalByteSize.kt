@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import kotlin.math.abs
import kotlin.math.absoluteValue
import me.saket.bytesize.internal.commonCompareTo
import me.saket.bytesize.internal.commonDiv
import me.saket.bytesize.internal.commonMinus
import me.saket.bytesize.internal.commonPlus
import me.saket.bytesize.internal.commonTimes
import me.saket.bytesize.internal.hasFractionalPart
import me.saket.bytesize.internal.toStringAsFixed

/**
 * Creates a [DecimalByteSize] from a raw number of bytes.
 *
 * Fractional values are rejected.
 */
@get:JvmSynthetic
inline val Number.decimalBytes: DecimalByteSize
  get() = DecimalByteSize(this)

/**
 * Returns a [DecimalByteSize] equal to this number of kilobytes.
 *
 * `1.kilobytes` is `1000.decimalBytes`.
 */
@get:JvmSynthetic
inline val Number.kilobytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerKB) * this

/**
 * Returns a [DecimalByteSize] equal to this number of megabytes.
 *
 * `1.megabytes` is `1000.kilobytes`.
 */
@get:JvmSynthetic
inline val Number.megabytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerMB) * this

/**
 * Returns a [DecimalByteSize] equal to this number of gigabytes.
 *
 * `1.gigabytes` is `1000.megabytes`.
 */
@get:JvmSynthetic
inline val Number.gigabytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerGB) * this

/** Returns this size without its sign. */
@get:JvmSynthetic
inline val DecimalByteSize.absoluteValue: DecimalByteSize
  get() = DecimalByteSize(inWholeBytes.absoluteValue)

/**
 * Represents a byte size in SI units.
 *
 * This type is typically used for manufacturer-reported storage units such as KB, MB, and GB.
 */
@JvmInline
value class DecimalByteSize(
  @PublishedApi
  @get:JvmSynthetic
  internal val bytes: Long,
) : ByteSize, BytePrecision {

  constructor(bytes: Number) : this(bytes.toLong()) {
    check(!bytes.hasFractionalPart()) { BytePrecisionLossErrorMessage }
  }

  @get:JvmName("inWholeBytes")
  override inline val inWholeBytes: Long
    get() = bytes

  @get:JvmName("inWholeKilobytes")
  inline val inWholeKilobytes: Long
    get() = inWholeBytes / BytesPerKB

  @get:JvmName("inWholeMegabytes")
  inline val inWholeMegabytes: Long
    get() = inWholeBytes / BytesPerMB

  @get:JvmName("inWholeGigabytes")
  inline val inWholeGigabytes: Long
    get() = inWholeBytes / BytesPerGB

  override inline operator fun plus(other: ByteSize): DecimalByteSize =
    DecimalByteSize(commonPlus(other))

  override inline operator fun minus(other: ByteSize): DecimalByteSize =
    DecimalByteSize(commonMinus(other))

  override inline fun times(other: Number): DecimalByteSize =
    DecimalByteSize(commonTimes(other))

  override inline fun div(other: ByteSize): Double =
    commonDiv(other)

  override inline fun div(other: Number): DecimalByteSize =
    DecimalByteSize(commonDiv(other))

  override inline operator fun unaryMinus(): DecimalByteSize =
    DecimalByteSize(-bytes)

  override inline fun compareTo(other: ByteSize): Int =
    commonCompareTo(other)

  override inline fun toString(): String {
    val sign = if (inWholeBytes < 0) "-" else ""
    val bytes = abs(inWholeBytes)
    return when {
      bytes < BytesPerKB -> "$sign${bytes.toStringAsFixed()} B"
      bytes < BytesPerMB -> "$sign${(bytes / BytesPerKB.toDouble()).toStringAsFixed()} KB"
      bytes < BytesPerGB -> "$sign${(bytes / BytesPerMB.toDouble()).toStringAsFixed()} MB"
      bytes < BytesPerTB -> "$sign${(bytes / BytesPerGB.toDouble()).toStringAsFixed()} GB"
      bytes < BytesPerPB -> "$sign${(bytes / BytesPerTB.toDouble()).toStringAsFixed()} TB"
      else -> "$sign${(bytes / BytesPerPB.toDouble()).toStringAsFixed()} PB"
    }
  }
}

@PublishedApi internal inline val BytesPerKB: Long get() = 1000L
@PublishedApi internal inline val BytesPerMB: Long get() = 1000L * BytesPerKB
@PublishedApi internal inline val BytesPerGB: Long get() = 1000L * BytesPerMB
@PublishedApi internal inline val BytesPerTB: Long get() = 1000L * BytesPerGB
@PublishedApi internal inline val BytesPerPB: Long get() = 1000L * BytesPerTB

@JvmSynthetic
inline operator fun Number.times(other: DecimalByteSize): DecimalByteSize {
  return other.times(this)
}
