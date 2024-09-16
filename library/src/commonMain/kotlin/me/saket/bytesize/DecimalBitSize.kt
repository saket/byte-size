@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import me.saket.bytesize.internal.BitsPerByte
import me.saket.bytesize.internal.commonCompareTo
import me.saket.bytesize.internal.commonDiv
import me.saket.bytesize.internal.commonMinus
import me.saket.bytesize.internal.commonPlus
import me.saket.bytesize.internal.commonTimes
import me.saket.bytesize.internal.hasFractionalPart
import me.saket.bytesize.internal.toStringAsFixed

@get:JvmSynthetic
inline val Number.decimalBits: DecimalBitSize
  get() = DecimalBitSize(this)

@get:JvmSynthetic
inline val Number.kilobits: DecimalBitSize
  get() = DecimalBitSize(BitsPerKb) * this

@get:JvmSynthetic
inline val Number.megabits: DecimalBitSize
  get() = DecimalBitSize(BitsPerMb) * this

@get:JvmSynthetic
inline val Number.gigabits: DecimalBitSize
  get() = DecimalBitSize(BitsPerGb) * this

/** Represents power-of-ten bit sizes. */
@JvmInline
value class DecimalBitSize(
  @PublishedApi
  @get:JvmSynthetic
  internal inline val bits: Long,
) : ByteSize, BitPrecision {

  constructor(bits: Number) : this(bits.toLong()) {
    check(!bits.hasFractionalPart()) { BitPrecisionLossErrorMessage }
  }

  @get:JvmName("inWholeBits")
  override inline val inWholeBits: Long
    get() = bits

  @get:JvmName("inWholeBytes")
  override inline val inWholeBytes: Long
    get() = bits / BitsPerByte

  @get:JvmName("inWholeKilobits")
  inline val inWholeKilobits: Long
    get() = inWholeBits / BitsPerKb

  @get:JvmName("inWholeMegabits")
  inline val inWholeMegabits: Long
    get() = inWholeBits / BitsPerMb

  @get:JvmName("inWholeGigabits")
  inline val inWholeGigabits: Long
    get() = inWholeBits / BitsPerGb

  override inline fun plus(other: ByteSize): DecimalBitSize {
    return DecimalBitSize(bits = commonPlus(other))
  }

  override inline fun minus(other: ByteSize): DecimalBitSize {
    return DecimalBitSize(bits = commonMinus(other))
  }

  override inline fun times(other: Number): DecimalBitSize {
    return DecimalBitSize(bits = commonTimes(other))
  }

  override inline fun div(other: ByteSize): Double {
    return commonDiv(other)
  }

  override inline fun div(other: Number): DecimalBitSize {
    return DecimalBitSize(bits = commonDiv(other))
  }

  override inline fun compareTo(other: ByteSize): Int {
    return commonCompareTo(other)
  }

  override inline fun toString(): String {
    return when {
      inWholeBits < BitsPerKb -> "$inWholeBits b"
      inWholeBits < BitsPerMb -> "${(inWholeBits / BitsPerKb.toDouble()).toStringAsFixed()} Kb"
      inWholeBits < BitsPerGb -> "${(inWholeBits / BitsPerMb.toDouble()).toStringAsFixed()} Mb"
      inWholeBits < BitsPerTb -> "${(inWholeBits / BitsPerGb.toDouble()).toStringAsFixed()} Gb"
      inWholeBits < BitsPerPb -> "${(inWholeBits / BitsPerTb.toDouble()).toStringAsFixed()} Tb"
      else -> "${(inWholeBits / BitsPerPb.toDouble()).toStringAsFixed()} Pb"
    }
  }
}

@PublishedApi internal inline val BitsPerKb: Long get() = 1000
@PublishedApi internal inline val BitsPerMb: Long get() = 1000 * BitsPerKb
@PublishedApi internal inline val BitsPerGb: Long get() = 1000 * BitsPerMb
@PublishedApi internal inline val BitsPerTb: Long get() = 1000 * BitsPerGb
@PublishedApi internal inline val BitsPerPb: Long get() = 1000 * BitsPerTb

@JvmSynthetic
inline operator fun Number.times(other: DecimalBitSize): DecimalBitSize {
  return other.times(this)
}
