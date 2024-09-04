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

@Deprecated(
  message = "Ambiguous unit and base. Use .decimalBytes or .binaryBytes instead.",
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Number.bytes: ByteSize
  get() { error("unreachable code") }

@PublishedApi
internal const val PrecisionLossErrorMessage =
  "ByteSize provides precision at the byte level. Representing a fractional value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using ByteSize."
