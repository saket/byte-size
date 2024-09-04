@file:Suppress("ConstPropertyName", "INAPPLICABLE_JVM_NAME", "FunctionName", "OVERRIDE_BY_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import me.saket.bytesize.DecimalByteSize.Companion.BytesPerGB
import me.saket.bytesize.DecimalByteSize.Companion.BytesPerKB
import me.saket.bytesize.DecimalByteSize.Companion.BytesPerMB

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

// todo: doc + mention .decimalBytes
// todo: verify java interop
@JvmInline
value class DecimalByteSize(
  @PublishedApi internal val bytes: Long,
) : ByteSize {

  constructor(bytes: Number) : this(bytes.toLong()) {
    check(!bytes.hasFractionalPart()) { PrecisionLossErrorMessage }
  }

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
      inWholeBytes < BytesPerKB -> "${inWholeBytes.toStringAsFixed()} bytes"
      inWholeBytes < BytesPerMB -> "${(inWholeBytes / BytesPerKB.toDouble()).toStringAsFixed()} KB"
      inWholeBytes < BytesPerGB -> "${(inWholeBytes / BytesPerMB.toDouble()).toStringAsFixed()} MB"
      else -> "${(inWholeBytes / BytesPerGB.toDouble()).toStringAsFixed()} GB"
    }
  }

  companion object {
    @PublishedApi internal inline val BytesPerKB: Long get() = 1000L
    @PublishedApi internal inline val BytesPerMB: Long get() = 1000L * BytesPerKB
    @PublishedApi internal inline val BytesPerGB: Long get() = 1000L * BytesPerMB
  }
}

@Deprecated(
  message = PrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toInt().decimalBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Float.decimalBytes: DecimalByteSize
  get() = error(PrecisionLossErrorMessage)

@Deprecated(
  message = PrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toLong().decimalBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Double.decimalBytes: DecimalByteSize
  get() = error(PrecisionLossErrorMessage)
