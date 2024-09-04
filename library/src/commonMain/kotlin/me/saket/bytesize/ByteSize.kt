@file:Suppress("ConstPropertyName", "INAPPLICABLE_JVM_NAME", "FunctionName")

package me.saket.bytesize

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.times

sealed interface ByteSize : Comparable<ByteSize> {
  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long

  operator fun plus(other: ByteSize): ByteSize
  operator fun minus(other: ByteSize): ByteSize
  operator fun times(other: Number): ByteSize
  operator fun div(other: ByteSize): Double
  operator fun div(other: Number): ByteSize
}

inline operator fun Number.times(other: ByteSize): ByteSize {
  // Delegating to each override explicitly so that they can be inlined.
  return when (other) {
    is BinaryByteSize -> other.times(this)
    is DecimalByteSize -> other.times(this)
  }
}

inline fun ByteSize.asBinaryBytes(): BinaryByteSize =
  when (this) {
    is BinaryByteSize -> this
    is DecimalByteSize -> BinaryByteSize(this.bytes)
  }

inline fun ByteSize.asDecimalBytes(): DecimalByteSize =
  when (this) {
    is BinaryByteSize -> DecimalByteSize(this.bytes)
    is DecimalByteSize -> this
  }
