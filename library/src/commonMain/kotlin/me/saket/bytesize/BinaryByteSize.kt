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
 * Creates a [BinaryByteSize] from a raw number of bytes.
 *
 * Fractional values are rejected.
 */
@get:JvmSynthetic
inline val Number.binaryBytes: BinaryByteSize
  get() = BinaryByteSize(this)

/**
 * Returns a [BinaryByteSize] equal to this number of kibibytes.
 *
 * `1.kibibytes` is `1024.binaryBytes`.
 */
@get:JvmSynthetic
inline val Number.kibibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerKiB) * this

/**
 * Returns a [BinaryByteSize] equal to this number of mebibytes.
 *
 * `1.mebibytes` is `1024.kibibytes`.
 */
@get:JvmSynthetic
inline val Number.mebibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerMiB) * this

/**
 * Returns a [BinaryByteSize] equal to this number of gibibytes.
 *
 * `1.gibibytes` is `1024.mebibytes`.
 */
@get:JvmSynthetic
inline val Number.gibibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerGiB) * this

/** Returns this size without its sign. */
@get:JvmSynthetic
inline val BinaryByteSize.absoluteValue: BinaryByteSize
  get() = BinaryByteSize(inWholeBytes.absoluteValue)

/**
 * Represents a byte size in binary units.
 *
 * This type is typically used for memory and operating system units such as KiB, MiB, and GiB.
 */
@JvmInline
value class BinaryByteSize(
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

  @get:JvmName("inWholeKibibytes")
  inline val inWholeKibibytes: Long
    get() = inWholeBytes / BytesPerKiB

  @get:JvmName("inWholeMebibytes")
  inline val inWholeMebibytes: Long
    get() = inWholeBytes / BytesPerMiB

  @get:JvmName("inWholeGibibytes")
  inline val inWholeGibibytes: Long
    get() = inWholeBytes / BytesPerGiB

  override inline operator fun plus(other: ByteSize): BinaryByteSize =
    BinaryByteSize(commonPlus(other))

  override inline operator fun minus(other: ByteSize): ByteSize =
    BinaryByteSize(commonMinus(other))

  override inline fun times(other: Number): BinaryByteSize =
    BinaryByteSize(commonTimes(other))

  override inline fun div(other: ByteSize): Double =
    commonDiv(other)

  override inline fun div(other: Number): BinaryByteSize =
    BinaryByteSize(commonDiv(other))

  override inline operator fun unaryMinus(): BinaryByteSize =
    BinaryByteSize(-bytes)

  override inline fun compareTo(other: ByteSize): Int =
    commonCompareTo(other)

  override inline fun toString(): String {
    val sign = if (inWholeBytes < 0) "-" else ""
    val bytes = abs(inWholeBytes)
    return when {
      bytes < BytesPerKiB -> "$sign${bytes.toStringAsFixed()} B"
      bytes < BytesPerMiB -> "$sign${(bytes / BytesPerKiB.toDouble()).toStringAsFixed()} KiB"
      bytes < BytesPerGiB -> "$sign${(bytes / BytesPerMiB.toDouble()).toStringAsFixed()} MiB"
      bytes < BytesPerTiB -> "$sign${(bytes / BytesPerGiB.toDouble()).toStringAsFixed()} GiB"
      bytes < BytesPerPiB -> "$sign${(bytes / BytesPerTiB.toDouble()).toStringAsFixed()} TiB"
      else -> "$sign${(bytes / BytesPerPiB.toDouble()).toStringAsFixed()} PiB"
    }
  }
}

@PublishedApi internal inline val BytesPerKiB: Long get() = 1024L
@PublishedApi internal inline val BytesPerMiB: Long get() = 1024L * BytesPerKiB
@PublishedApi internal inline val BytesPerGiB: Long get() = 1024L * BytesPerMiB
@PublishedApi internal inline val BytesPerTiB: Long get() = 1024L * BytesPerGiB
@PublishedApi internal inline val BytesPerPiB: Long get() = 1024L * BytesPerTiB

@JvmSynthetic
inline operator fun Number.times(other: BinaryByteSize): BinaryByteSize {
  return other.times(this)
}
