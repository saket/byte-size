@file:Suppress("ConstPropertyName", "INAPPLICABLE_JVM_NAME", "FunctionName")

package me.saket.bytesize

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

sealed interface ByteSize : Comparable<ByteSize> {
  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long

  operator fun plus(other: ByteSize): ByteSize
  operator fun minus(other: ByteSize): ByteSize
  operator fun times(other: Number): ByteSize
  operator fun div(other: ByteSize): Double
  operator fun div(other: Number): ByteSize
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
