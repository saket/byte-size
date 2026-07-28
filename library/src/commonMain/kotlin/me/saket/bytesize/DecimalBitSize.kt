@file:Suppress("INAPPLICABLE_JVM_NAME", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package me.saket.bytesize

import dev.erikchristensen.javamath2kmp.minusExact
import dev.erikchristensen.javamath2kmp.plusExact
import dev.erikchristensen.javamath2kmp.timesExact
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import kotlin.math.abs
import kotlin.math.absoluteValue
import me.saket.bytesize.internal.BitsPerByte
import me.saket.bytesize.internal.commonCompareTo
import me.saket.bytesize.internal.commonDiv
import me.saket.bytesize.internal.commonMinus
import me.saket.bytesize.internal.commonPlus
import me.saket.bytesize.internal.commonTimes
import me.saket.bytesize.internal.hasFractionalPart
import me.saket.bytesize.internal.toStringAsFixed

/**
 * Creates a [DecimalBitSize] from a raw number of bits.
 *
 * Fractional values are rejected.
 */
@get:JvmSynthetic
inline val Number.decimalBits: DecimalBitSize
  get() = DecimalBitSize(this)

/** Allocation-free overload of [decimalBits] for [Int] receivers. */
@get:JvmSynthetic
inline val Int.decimalBits: DecimalBitSize
  get() = DecimalBitSize(bits = toLong())

/** Allocation-free overload of [decimalBits] for [Long] receivers. */
@get:JvmSynthetic
inline val Long.decimalBits: DecimalBitSize
  get() = DecimalBitSize(bits = this)

/**
 * Returns a [DecimalBitSize] equal to this number of kilobits.
 *
 * `1.kilobits` is `1000.decimalBits`.
 */
@get:JvmSynthetic
inline val Number.kilobits: DecimalBitSize
  get() = DecimalBitSize(BitsPerKb) * this

/** Allocation-free overload of [kilobits] for [Int] receivers. */
@get:JvmSynthetic
inline val Int.kilobits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerKb.timesExact(toLong()))

/** Allocation-free overload of [kilobits] for [Long] receivers. */
@get:JvmSynthetic
inline val Long.kilobits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerKb.timesExact(this))

/**
 * Returns a [DecimalBitSize] equal to this number of megabits.
 *
 * `1.megabits` is `1000.kilobits`.
 */
@get:JvmSynthetic
inline val Number.megabits: DecimalBitSize
  get() = DecimalBitSize(BitsPerMb) * this

/** Allocation-free overload of [megabits] for [Int] receivers. */
@get:JvmSynthetic
inline val Int.megabits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerMb.timesExact(toLong()))

/** Allocation-free overload of [megabits] for [Long] receivers. */
@get:JvmSynthetic
inline val Long.megabits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerMb.timesExact(this))

/**
 * Returns a [DecimalBitSize] equal to this number of gigabits.
 *
 * `1.gigabits` is `1000.megabits`.
 */
@get:JvmSynthetic
inline val Number.gigabits: DecimalBitSize
  get() = DecimalBitSize(BitsPerGb) * this

/** Allocation-free overload of [gigabits] for [Int] receivers. */
@get:JvmSynthetic
inline val Int.gigabits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerGb.timesExact(toLong()))

/** Allocation-free overload of [gigabits] for [Long] receivers. */
@get:JvmSynthetic
inline val Long.gigabits: DecimalBitSize
  get() = DecimalBitSize(bits = BitsPerGb.timesExact(this))

/** Returns this size without its sign. */
@get:JvmSynthetic
inline val DecimalBitSize.absoluteValue: DecimalBitSize
  get() = DecimalBitSize(inWholeBits.absoluteValue)

/**
 * Represents a bit size in SI units.
 *
 * This type is typically used for network-style units such as Kb, Mb, and Gb.
 */
@JvmInline
value class DecimalBitSize(
  @PublishedApi
  @get:JvmSynthetic
  internal val bits: Long,
) : ByteSize, BitPrecision {

  constructor(bits: Number) : this(bits.toLong()) {
    check(!bits.hasFractionalPart()) { BitPrecisionLossErrorMessage }
  }

  @get:JvmName("inWholeBits")
  override inline val inWholeBits: Long
    get() = bits

  /** Returns the whole-byte portion of this bit size, truncated toward zero. */
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

  /** Allocation-free overload of [plus] for same-precision operands. */
  inline operator fun plus(other: DecimalBitSize): DecimalBitSize {
    return DecimalBitSize(bits = bits.plusExact(other.bits))
  }

  override inline fun minus(other: ByteSize): DecimalBitSize {
    return DecimalBitSize(bits = commonMinus(other))
  }

  /** Allocation-free overload of [minus] for same-precision operands. */
  inline operator fun minus(other: DecimalBitSize): DecimalBitSize {
    return DecimalBitSize(bits = bits.minusExact(other.bits))
  }

  override inline fun times(other: Number): DecimalBitSize {
    return DecimalBitSize(bits = commonTimes(other))
  }

  override inline fun div(other: ByteSize): Double {
    return commonDiv(other)
  }

  /** Allocation-free overload of [div] for same-precision operands. */
  inline operator fun div(other: DecimalBitSize): Double {
    return bits.toDouble() / other.bits
  }

  override inline fun div(other: Number): DecimalBitSize {
    return DecimalBitSize(bits = commonDiv(other))
  }

  override inline operator fun unaryMinus(): DecimalBitSize =
    DecimalBitSize(-bits)

  override inline fun compareTo(other: ByteSize): Int {
    return commonCompareTo(other)
  }

  /** Allocation-free overload of [compareTo] for same-precision operands. */
  inline operator fun compareTo(other: DecimalBitSize): Int {
    return bits.compareTo(other.bits)
  }

  override inline fun toString(): String {
    val sign = if (inWholeBits < 0) "-" else ""
    val bits = abs(inWholeBits)
    return when {
      bits < BitsPerKb -> "$sign$bits b"
      bits < BitsPerMb -> "$sign${(bits / BitsPerKb.toDouble()).toStringAsFixed()} Kb"
      bits < BitsPerGb -> "$sign${(bits / BitsPerMb.toDouble()).toStringAsFixed()} Mb"
      bits < BitsPerTb -> "$sign${(bits / BitsPerGb.toDouble()).toStringAsFixed()} Gb"
      bits < BitsPerPb -> "$sign${(bits / BitsPerTb.toDouble()).toStringAsFixed()} Tb"
      else -> "$sign${(bits / BitsPerPb.toDouble()).toStringAsFixed()} Pb"
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
