@file:Suppress("INAPPLICABLE_JVM_NAME", "NOTHING_TO_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmName
import me.saket.bytesize.internal.BitsPerByte

/**
 * Represents a size that can be expressed in bytes.
 *
 * A [ByteSize] is one of [BinaryByteSize], [DecimalByteSize], or [DecimalBitSize].
 */
sealed interface ByteSize : Comparable<ByteSize> {
  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long

  operator fun plus(other: ByteSize): ByteSize
  operator fun minus(other: ByteSize): ByteSize
  operator fun times(other: Number): ByteSize
  operator fun div(other: ByteSize): Double
  operator fun div(other: Number): ByteSize
  operator fun unaryMinus(): ByteSize
}

/** Returns the result of multiplying [other] by this number. */
inline operator fun Number.times(other: ByteSize): ByteSize =
  when (other) {
    is BinaryByteSize -> this.times(other)
    is DecimalByteSize -> this.times(other)
    is DecimalBitSize -> this.times(other)
  }

/**
 * Converts this size to binary byte units such as KiB, MiB, and GiB.
 *
 * `700.kilobytes.toBinaryBytes()` is `683.kibibytes`.
 */
inline fun ByteSize.toBinaryBytes(): BinaryByteSize =
  this as? BinaryByteSize ?: BinaryByteSize(this.inWholeBytes)

/**
 * Converts this size to decimal byte units such as KB, MB, and GB.
 *
 * `256.kibibytes.toDecimalBytes()` is `262.kilobytes`.
 */
inline fun ByteSize.toDecimalBytes(): DecimalByteSize =
  this as? DecimalByteSize ?: DecimalByteSize(this.inWholeBytes)

/**
 * Converts this size to decimal bit units such as Kb, Mb, and Gb.
 *
 * `1.gigabytes.toDecimalBits()` is `8.gigabits`.
 */
inline fun ByteSize.toDecimalBits(): DecimalBitSize =
  this as? DecimalBitSize ?: DecimalBitSize(this.inWholeBytes * BitsPerByte)

/** Returns this size without its sign, preserving its concrete subtype. */
inline val ByteSize.absoluteValue: ByteSize
  get() =
    when (this) {
      is DecimalBitSize -> this.absoluteValue
      is BinaryByteSize -> this.absoluteValue
      is DecimalByteSize -> this.absoluteValue
    }
