@file:Suppress("unused", "UnusedReceiverParameter", "UNUSED_PARAMETER")

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
  message = BytePrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toInt().binaryBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Float.binaryBytes: BinaryByteSize
  get() = error(BytePrecisionLossErrorMessage)

@Deprecated(
  message = BytePrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toLong().binaryBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Double.binaryBytes: BinaryByteSize
  get() = error(BytePrecisionLossErrorMessage)

@Deprecated(
  message = BytePrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toInt().decimalBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Float.decimalBytes: DecimalByteSize
  get() = error(BytePrecisionLossErrorMessage)

@Deprecated(
  message = BytePrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toLong().decimalBytes"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Double.decimalBytes: DecimalByteSize
  get() = error(BytePrecisionLossErrorMessage)

@Deprecated(
  message = BitPrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toInt().decimalBits"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Float.decimalBits: DecimalByteSize
  get() = error(BitPrecisionLossErrorMessage)

@Deprecated(
  message = BitPrecisionLossErrorMessage,
  replaceWith = ReplaceWith("toLong().decimalBits"),
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
val Double.decimalBits: DecimalByteSize
  get() = error(BitPrecisionLossErrorMessage)

@PublishedApi
internal const val BytePrecisionLossErrorMessage =
  "(Binary/Decimal)ByteSize provides precision at the byte level. Representing a fractional value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using (Binary/Decimal)ByteSize."

@PublishedApi
internal const val BitPrecisionLossErrorMessage =
  "(Binary/Decimal)BitSize provide precision at the bit level. Representing a fractional value as " +
    "bits may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using (Binary/Decimal)BitSize."
