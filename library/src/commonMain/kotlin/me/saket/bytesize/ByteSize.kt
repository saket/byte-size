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
import me.saket.bytesize.ByteSize.Companion.BytesPerGib
import me.saket.bytesize.ByteSize.Companion.BytesPerKB
import me.saket.bytesize.ByteSize.Companion.BytesPerKib
import me.saket.bytesize.ByteSize.Companion.BytesPerMB
import me.saket.bytesize.ByteSize.Companion.BytesPerMib

// todo: doc
// todo: @JvmName required?
inline fun BinaryByteSize(bytes: Number): ByteSize {
  check(bytes !is Float && bytes !is Double) { PrecisionLossErrorMessage }
  return ByteSize(packValue(bytes.toLong(), DataStorageUnit.BinaryBytes))
}

// todo: doc
// todo: @JvmName required?
inline fun DecimalByteSize(bytes: Number): ByteSize {
  check(bytes !is Float && bytes !is Double) { PrecisionLossErrorMessage }
  return ByteSize(packValue(bytes.toLong(), DataStorageUnit.DecimalBytes))
}

@JvmInline
value class ByteSize @PublishedApi internal constructor(
  @PublishedApi internal val packedValue: Long,
) : Comparable<ByteSize> {

  @PublishedApi
  internal inline val storageUnit: DataStorageUnit
    get() = unpackStorageUnit(packedValue)

  @get:JvmName("inWholeBytes")
  inline val inWholeBytes: Long
    get() = unpackBytesValue(packedValue)

  @get:JvmName("inWholeKilobytes")
  inline val inWholeKilobytes: Long
    get() = inWholeBytes / BytesPerKB

  @get:JvmName("inWholeMegabytes")
  inline val inWholeMegabytes: Long
    get() = inWholeBytes / BytesPerMB

  @get:JvmName("inWholeGigabytes")
  inline val inWholeGigabytes: Long
    get() = inWholeBytes / BytesPerGB

  override operator fun compareTo(other: ByteSize): Int {
    // todo: this should be using the packed value somehow
    return inWholeBytes.compareTo(other.inWholeBytes)
  }

  inline operator fun plus(other: ByteSize): ByteSize =
    ByteSize(inWholeBytes.plusExact(other.inWholeBytes))

  inline operator fun minus(other: ByteSize): ByteSize {
    return ByteSize(inWholeBytes.minusExact(other.inWholeBytes))
  }

  inline operator fun times(other: ByteSize): ByteSize =
    this * other.inWholeBytes

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
    return ByteSize(packValue(result, storageUnit))
  }

  inline operator fun div(other: ByteSize): ByteSize =
    this / other.inWholeBytes

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
    return ByteSize(packValue(result, storageUnit))
  }

  override fun toString(): String {
    return when {
      inWholeBytes < BytesPerKB -> "${inWholeBytes.toStringAsFixed()} bytes"
      inWholeBytes < BytesPerMB -> "${(inWholeBytes / BytesPerKB.toDouble()).toStringAsFixed()} KB"
      inWholeBytes < BytesPerGB -> "${(inWholeBytes / BytesPerMB.toDouble()).toStringAsFixed()} MB"
      else -> "${(inWholeBytes / BytesPerGB.toDouble()).toStringAsFixed()} GB"
    }
  }

  companion object {
    @PublishedApi internal const val BytesPerKB: Long = 1000L
    @PublishedApi internal const val BytesPerMB: Long = 1000L * BytesPerKB
    @PublishedApi internal const val BytesPerGB: Long = 1000L * BytesPerMB

    @PublishedApi internal const val BytesPerKib: Long = 1024L
    @PublishedApi internal const val BytesPerMib: Long = 1024L * BytesPerKib
    @PublishedApi internal const val BytesPerGib: Long = 1024L * BytesPerMib
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
  get() = BinaryByteSize(BytesPerKib) * this

@get:JvmSynthetic
inline val Number.megabytes: ByteSize
  get() = DecimalByteSize(BytesPerMB) * this

@get:JvmSynthetic
inline val Number.mebibytes: ByteSize
  get() = BinaryByteSize(BytesPerMib) * this

@get:JvmSynthetic
inline val Number.gigabytes: ByteSize
  get() = DecimalByteSize(BytesPerGB) * this

inline val Number.gibibytes: ByteSize
  get() = BinaryByteSize(BytesPerGib) * this

@PublishedApi
internal const val PrecisionLossErrorMessage =
  "ByteSize provides precision at the byte level. Representing a fractional value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using ByteSize."
