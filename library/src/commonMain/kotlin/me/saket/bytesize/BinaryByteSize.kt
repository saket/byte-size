@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import me.saket.bytesize.BinaryByteSize.Companion.BytesPerGiB
import me.saket.bytesize.BinaryByteSize.Companion.BytesPerKiB
import me.saket.bytesize.BinaryByteSize.Companion.BytesPerMiB
import me.saket.bytesize.internal.commonCompareTo
import me.saket.bytesize.internal.commonDiv
import me.saket.bytesize.internal.commonMinus
import me.saket.bytesize.internal.commonPlus
import me.saket.bytesize.internal.commonTimes
import me.saket.bytesize.internal.hasFractionalPart
import me.saket.bytesize.internal.toStringAsFixed

@get:JvmSynthetic
inline val Number.binaryBytes: BinaryByteSize
  get() = BinaryByteSize(this)

@get:JvmSynthetic
inline val Number.kibibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerKiB) * this

@get:JvmSynthetic
inline val Number.mebibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerMiB) * this

@get:JvmSynthetic
inline val Number.gibibytes: BinaryByteSize
  get() = BinaryByteSize(BytesPerGiB) * this

/** Represents power-of-two byte sizes. */
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

  override inline fun compareTo(other: ByteSize): Int =
    commonCompareTo(other)

  override inline fun toString(): String {
    return when {
      inWholeBytes < BytesPerKiB -> "${inWholeBytes.toStringAsFixed()} B"
      inWholeBytes < BytesPerMiB -> "${(inWholeBytes / BytesPerKiB.toDouble()).toStringAsFixed()} KiB"
      inWholeBytes < BytesPerGiB -> "${(inWholeBytes / BytesPerMiB.toDouble()).toStringAsFixed()} MiB"
      else -> "${(inWholeBytes / BytesPerGiB.toDouble()).toStringAsFixed()} GiB"
    }
  }

  companion object {
    @PublishedApi internal inline val BytesPerKiB: Long get() = 1024L
    @PublishedApi internal inline val BytesPerMiB: Long get() = 1024L * BytesPerKiB
    @PublishedApi internal inline val BytesPerGiB: Long get() = 1024L * BytesPerMiB
  }
}

@JvmSynthetic
inline operator fun Number.times(other: BinaryByteSize): BinaryByteSize {
  return other.times(this)
}
