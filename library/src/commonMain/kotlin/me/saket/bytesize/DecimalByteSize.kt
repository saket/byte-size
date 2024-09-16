@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import me.saket.bytesize.internal.commonCompareTo
import me.saket.bytesize.internal.commonDiv
import me.saket.bytesize.internal.commonMinus
import me.saket.bytesize.internal.commonPlus
import me.saket.bytesize.internal.commonTimes
import me.saket.bytesize.internal.hasFractionalPart
import me.saket.bytesize.internal.toStringAsFixed

@get:JvmSynthetic
inline val Number.decimalBytes: DecimalByteSize
  get() = DecimalByteSize(this)

@get:JvmSynthetic
inline val Number.kilobytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerKB) * this

@get:JvmSynthetic
inline val Number.megabytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerMB) * this

@get:JvmSynthetic
inline val Number.gigabytes: DecimalByteSize
  get() = DecimalByteSize(BytesPerGB) * this

/** Represents power-of-ten byte sizes. */
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

  override inline fun compareTo(other: ByteSize): Int =
    commonCompareTo(other)

  override inline fun toString(): String {
    return when {
      inWholeBytes < BytesPerKB -> "${inWholeBytes.toStringAsFixed()} B"
      inWholeBytes < BytesPerMB -> "${(inWholeBytes / BytesPerKB.toDouble()).toStringAsFixed()} KB"
      inWholeBytes < BytesPerGB -> "${(inWholeBytes / BytesPerMB.toDouble()).toStringAsFixed()} MB"
      inWholeBytes < BytesPerTB -> "${(inWholeBytes / BytesPerGB.toDouble()).toStringAsFixed()} GB"
      inWholeBytes < BytesPerPB -> "${(inWholeBytes / BytesPerTB.toDouble()).toStringAsFixed()} TB"
      else -> "${(inWholeBytes / BytesPerPB.toDouble()).toStringAsFixed()} PB"
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
