package me.saket.bytesize

import kotlin.jvm.JvmSynthetic

@Deprecated(
  message = "Ambiguous unit and base. Use .decimalBytes or .binaryBytes instead.",
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Number.bytes: ByteSize
  get() { error("unreachable code") }

@Deprecated(
  message = "Ambiguous unit and base. Use DecimalBytes() or BinaryBytes() instead.",
  level = DeprecationLevel.ERROR,
)
@JvmSynthetic
fun ByteSize(bytes: Long): ByteSize {
  error("unreachable code")
}

@Deprecated(
  message = PrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toInt().binaryBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Float.binaryBytes: BinaryByteSize
  get() = error(PrecisionLossErrorMessage)

@Deprecated(
  message = PrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toLong().binaryBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Double.binaryBytes: BinaryByteSize
  get() = error(PrecisionLossErrorMessage)

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

@PublishedApi
internal const val PrecisionLossErrorMessage =
  "ByteSize provides precision at the byte level. Representing a fractional value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using ByteSize."
