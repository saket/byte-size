@file:Suppress("INAPPLICABLE_JVM_NAME")

package me.saket.bytesize

import kotlin.jvm.JvmName
import me.saket.bytesize.internal.BitsPerByte

sealed interface ByteSize : Comparable<ByteSize> {
  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long  // todo: verify JVM interop for sub-classes

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

inline fun ByteSize.asBinaryBytes(): BinaryByteSize =
  if (this is BinaryByteSize) this else BinaryByteSize(this.inWholeBytes)

inline fun ByteSize.asDecimalBytes(): DecimalByteSize =
  if (this is DecimalByteSize) this else DecimalByteSize(this.inWholeBytes)

inline fun ByteSize.asDecimalBits(): DecimalBitSize =
  if (this is DecimalBitSize) this else DecimalBitSize(this.inWholeBytes * BitsPerByte)

@PublishedApi
internal sealed interface BytePrecision {
  val inWholeBytes: Long
}

@PublishedApi
internal sealed interface BitPrecision {
  val inWholeBits: Long
}
