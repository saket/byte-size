@file:Suppress("INAPPLICABLE_JVM_NAME", "NOTHING_TO_INLINE")

package me.saket.bytesize

import kotlin.jvm.JvmName
import me.saket.bytesize.internal.BitsPerByte

/**
 * Represents a size which can be converted into bytes.
 *
 * See:
 * - [BinaryByteSize]
 * - [DecimalByteSize]
 * - [DecimalBitSize]
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

inline operator fun Number.times(other: ByteSize): ByteSize =
  when (other) {
    is BinaryByteSize -> this.times(other)
    is DecimalByteSize -> this.times(other)
    is DecimalBitSize -> this.times(other)
  }

inline fun ByteSize.toBinaryBytes(): BinaryByteSize =
  this as? BinaryByteSize ?: BinaryByteSize(this.inWholeBytes)

inline fun ByteSize.toDecimalBytes(): DecimalByteSize =
  this as? DecimalByteSize ?: DecimalByteSize(this.inWholeBytes)

inline fun ByteSize.toDecimalBits(): DecimalBitSize =
  this as? DecimalBitSize ?: DecimalBitSize(this.inWholeBytes * BitsPerByte)
