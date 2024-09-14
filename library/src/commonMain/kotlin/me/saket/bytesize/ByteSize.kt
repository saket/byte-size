@file:Suppress("INAPPLICABLE_JVM_NAME")

package me.saket.bytesize

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
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
}

inline operator fun Number.times(other: ByteSize): ByteSize =
  when (other) {
    is BinaryByteSize -> this.times(other)
    is DecimalByteSize -> this.times(other)
    is DecimalBitSize -> this.times(other)
  }

inline fun ByteSize.toBinaryBytes(): BinaryByteSize =
  if (this is BinaryByteSize) this else BinaryByteSize(this.inWholeBytes)

inline fun ByteSize.toDecimalBytes(): DecimalByteSize =
  if (this is DecimalByteSize) this else DecimalByteSize(this.inWholeBytes)

inline fun ByteSize.toDecimalBits(): DecimalBitSize =
  if (this is DecimalBitSize) this else DecimalBitSize(this.inWholeBytes * BitsPerByte)
