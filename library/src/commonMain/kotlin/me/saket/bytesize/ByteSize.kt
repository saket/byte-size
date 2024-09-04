@file:Suppress("ConstPropertyName", "INAPPLICABLE_JVM_NAME", "FunctionName")

package me.saket.bytesize

import dev.erikchristensen.javamath2kmp.minusExact
import dev.erikchristensen.javamath2kmp.plusExact
import dev.erikchristensen.javamath2kmp.timesExact
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import kotlin.math.roundToLong
import me.saket.bytesize.ByteSize.Companion.BytesPerGB
import me.saket.bytesize.ByteSize.Companion.BytesPerGiB
import me.saket.bytesize.ByteSize.Companion.BytesPerKB
import me.saket.bytesize.ByteSize.Companion.BytesPerKiB
import me.saket.bytesize.ByteSize.Companion.BytesPerMB
import me.saket.bytesize.ByteSize.Companion.BytesPerMiB

// todo: doc + mention .binaryBytes
// todo: @JvmName required?
inline fun BinaryByteSize(bytes: Number): ByteSize {
  if (bytes.hasFractionalPart()) {
    error(PrecisionLossErrorMessage)
  }
  return ByteSize(packValue(bytes.toLong(), DataMeasurementUnit.BinaryBytes))
}

// todo: doc + mention .decimalBytes
// todo: @JvmName required?
inline fun DecimalByteSize(bytes: Number): ByteSize {
  if (bytes.hasFractionalPart()) {
    error(PrecisionLossErrorMessage)
  }
  return ByteSize(packValue(bytes.toLong(), DataMeasurementUnit.DecimalBytes))
}

@JvmInline
value class ByteSize @PublishedApi internal constructor(
  @PublishedApi internal val packedValue: Long,
) : Comparable<ByteSize> {

  @PublishedApi
  internal inline val measurementUnit: DataMeasurementUnit
    get() = unpackMeasurementUnit(packedValue)

  @get:JvmName("inWholeBytes")
  inline val inWholeBytes: Long
    get() = unpackBytesValue(packedValue)

  @get:JvmName("inWholeKilobytes")
  inline val inWholeKilobytes: Long get() = inWholeBytes / BytesPerKB

  @get:JvmName("inWholeKibibytes")
  inline val inWholeKibibytes: Long get() = inWholeBytes / BytesPerKiB

  @get:JvmName("inWholeMegabytes")
  inline val inWholeMegabytes: Long get() = inWholeBytes / BytesPerMB

  @get:JvmName("inWholeMebibytes")
  inline val inWholeMebibytes: Long get() = inWholeBytes / BytesPerMiB

  @get:JvmName("inWholeGigabytes")
  inline val inWholeGigabytes: Long get() = inWholeBytes / BytesPerGB

  @get:JvmName("inWholeGibibytes")
  inline val inWholeGibibytes: Long get() = inWholeBytes / BytesPerGiB

  override operator fun compareTo(other: ByteSize): Int {
    return inWholeBytes.compareTo(other.inWholeBytes)
  }

  inline operator fun plus(other: ByteSize): ByteSize {
    return copy(inWholeBytes.plusExact(other.inWholeBytes))
  }

  inline operator fun minus(other: ByteSize): ByteSize {
    return copy(inWholeBytes.minusExact(other.inWholeBytes))
  }

  inline operator fun times(other: Number): ByteSize {
    val result: Long = when (other) {
      is Byte,
      is Short,
      is Int,
      is Long,
      -> inWholeBytes.timesExact(other.toLong())
      is Float -> inWholeBytes.timesExact(other.toDouble()).toLongOrThrow()
      is Double -> inWholeBytes.timesExact(other).toLongOrThrow()
      else -> error("Unsupported number: ${other::class}")
    }
    return copy(result)
  }

  inline operator fun div(other: ByteSize): Double =
    this.inWholeBytes.toDouble() / other.inWholeBytes

  inline operator fun div(other: Number): ByteSize {
    val result: Long = when (other) {
      is Byte -> inWholeBytes / other
      is Short -> inWholeBytes / other
      is Int -> inWholeBytes / other
      is Long -> inWholeBytes / other
      is Float -> (inWholeBytes / other).roundToLong()
      is Double -> (inWholeBytes / other).roundToLong()
      else -> error("Unsupported number: ${other::class}")
    }
    return copy(result)
  }

  @PublishedApi
  internal inline fun copy(bytes: Long): ByteSize {
    return ByteSize(packValue(bytes, measurementUnit))
  }

  override fun toString(): String {
    return when (measurementUnit) {
      DataMeasurementUnit.BinaryBytes -> when {
        inWholeBytes < BytesPerKiB -> "${inWholeBytes.toStringAsFixed()} bytes"
        inWholeBytes < BytesPerMiB -> "${(inWholeBytes / BytesPerKiB.toDouble()).toStringAsFixed()} KiB"
        inWholeBytes < BytesPerGiB -> "${(inWholeBytes / BytesPerMiB.toDouble()).toStringAsFixed()} MiB"
        else -> "${(inWholeBytes / BytesPerGiB.toDouble()).toStringAsFixed()} GiB"
      }
      DataMeasurementUnit.DecimalBytes -> when {
        inWholeBytes < BytesPerKB -> "${inWholeBytes.toStringAsFixed()} bytes"
        inWholeBytes < BytesPerMB -> "${(inWholeBytes / BytesPerKB.toDouble()).toStringAsFixed()} KB"
        inWholeBytes < BytesPerGB -> "${(inWholeBytes / BytesPerMB.toDouble()).toStringAsFixed()} MB"
        else -> "${(inWholeBytes / BytesPerGB.toDouble()).toStringAsFixed()} GB"
      }
      DataMeasurementUnit.BinaryBits -> error("unsupported")
      DataMeasurementUnit.DecimalBits -> error("unsupported")
    }
  }

  companion object {
    @PublishedApi internal const val BytesPerKB: Long = 1000L
    @PublishedApi internal const val BytesPerMB: Long = 1000L * BytesPerKB
    @PublishedApi internal const val BytesPerGB: Long = 1000L * BytesPerMB

    @PublishedApi internal const val BytesPerKiB: Long = 1024L
    @PublishedApi internal const val BytesPerMiB: Long = 1024L * BytesPerKiB
    @PublishedApi internal const val BytesPerGiB: Long = 1024L * BytesPerMiB
  }
}

@Deprecated(
  message = "Ambiguous base. Use .decimalBytes or .binaryBytes instead.",
  level = DeprecationLevel.ERROR,
)
@get:JvmSynthetic
inline val Number.bytes: ByteSize
  get() { error("unreachable code") }

@get:JvmSynthetic
inline val Number.decimalBytes: ByteSize
  get() = DecimalByteSize(this)

@get:JvmSynthetic
inline val Number.binaryBytes: ByteSize
  get() = BinaryByteSize(this)

@get:JvmSynthetic
inline val Number.kilobytes: ByteSize
  get() = DecimalByteSize(BytesPerKB) * this

@get:JvmSynthetic
inline val Number.kibibytes: ByteSize
  get() = BinaryByteSize(BytesPerKiB) * this

@get:JvmSynthetic
inline val Number.megabytes: ByteSize
  get() = DecimalByteSize(BytesPerMB) * this

@get:JvmSynthetic
inline val Number.mebibytes: ByteSize
  get() = BinaryByteSize(BytesPerMiB) * this

@get:JvmSynthetic
inline val Number.gigabytes: ByteSize
  get() = DecimalByteSize(BytesPerGB) * this

inline val Number.gibibytes: ByteSize
  get() = BinaryByteSize(BytesPerGiB) * this

@PublishedApi
internal const val PrecisionLossErrorMessage =
  "ByteSize provides precision at the byte level. Representing a fractional value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using ByteSize."
