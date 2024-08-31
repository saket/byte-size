@file:Suppress("ConstPropertyName")

package me.saket.bytesize

import dev.erikchristensen.javamath2kmp.minusExact
import dev.erikchristensen.javamath2kmp.plusExact
import dev.erikchristensen.javamath2kmp.timesExact
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic
import kotlin.math.roundToLong

@JvmInline
value class ByteSize(
  @PublishedApi internal val bytes: Long,
) : Comparable<ByteSize> {

  @get:JvmName("inWholeBytes")
  inline val inWholeBytes: Long
    get() = bytes

  @get:JvmName("inWholeKilobytes")
  inline val inWholeKilobytes: Long
    get() = inWholeBytes / BytesPerKb

  @get:JvmName("inWholeMegabytes")
  inline val inWholeMegabytes: Long
    get() = inWholeBytes / BytesPerMb

  @get:JvmName("inWholeGigabytes")
  inline val inWholeGigabytes: Long
    get() = inWholeBytes / BytesPerGb

  override operator fun compareTo(other: ByteSize): Int =
    inWholeBytes.compareTo(other.inWholeBytes)

  inline operator fun plus(other: ByteSize): ByteSize =
    ByteSize(inWholeBytes.plusExact(other.inWholeBytes))

  inline operator fun minus(other: ByteSize): ByteSize =
    ByteSize(inWholeBytes.minusExact(other.inWholeBytes))

  inline operator fun times(other: ByteSize): ByteSize =
    this * other.bytes

  inline operator fun times(other: Number): ByteSize {
    val result: Long = when (other) {
      is Byte,
      is Short,
      is Int,
      is Long, -> bytes.timesExact(other.toLong())
      is Float -> bytes.timesExact(other.toDouble()).toLongOrThrow()
      is Double -> bytes.timesExact(other).toLongOrThrow()
      else -> error("Unsupported number: ${other::class}")
    }
    return ByteSize(bytes = result)
  }

  inline operator fun div(other: ByteSize): ByteSize =
    this / other.bytes

  inline operator fun div(other: Number): ByteSize {
    val result: Long = when (other) {
      is Byte -> bytes / other
      is Short -> bytes / other
      is Int -> bytes / other
      is Long -> bytes / other
      is Float -> (bytes / other).roundToLong()
      is Double -> (bytes / other).roundToLong()
      else -> error("Unsupported number: ${other::class}")
    }
    return ByteSize(bytes = result)
  }

  override fun toString(): String {
    return when {
      inWholeBytes < BytesPerKb -> "${inWholeBytes.toStringAsFixed()} bytes"
      inWholeBytes < BytesPerMb -> "${(inWholeBytes / BytesPerKb.toDouble()).toStringAsFixed()} KB"
      inWholeBytes < BytesPerGb -> "${(inWholeBytes / BytesPerMb.toDouble()).toStringAsFixed()} MB"
      else -> "${(inWholeBytes / BytesPerGb.toDouble()).toStringAsFixed()} GB"
    }
  }

  companion object {
    val Zero: ByteSize get() = ByteSize(0L)

    @PublishedApi internal const val BytesPerKb: Long = 1_000L
    @PublishedApi internal const val BytesPerMb: Long = 1_000L * BytesPerKb
    @PublishedApi internal const val BytesPerGb: Long = 1_000L * BytesPerMb
  }
}

@get:JvmName("bytes")
inline val Number.bytes: ByteSize
  get() {
    if (this is Double) {
      // Double#bytes is already a compilation error. This runtime error prevents
      // developers from casting doubles as Numbers to bypass the compilation error.
      error(PrecisionLossErrorMessage)
    }
    return ByteSize(bytes = this.toLong())
  }

@get:JvmName("kilobytes")
inline val Number.kilobytes: ByteSize
  get() = ByteSize(bytes = ByteSize.BytesPerKb) * this

@get:JvmName("megabytes")
inline val Number.megabytes: ByteSize
  get() = ByteSize(bytes = ByteSize.BytesPerMb) * this

@get:JvmName("gigabytes")
inline val Number.gigabytes: ByteSize
  get() = ByteSize(bytes = ByteSize.BytesPerGb) * this

@get:JvmSynthetic
@Deprecated(PrecisionLossErrorMessage, level = DeprecationLevel.ERROR)
@Suppress("DeprecatedCallableAddReplaceWith")
val Double.bytes: ByteSize
  get() = error(PrecisionLossErrorMessage)

@PublishedApi
internal const val PrecisionLossErrorMessage =
  "ByteSize provides precision at the byte level. Representing a fractional Double value as " +
    "bytes may lead to precision loss. It is recommended to convert the value to a whole " +
    "number before using ByteSize."
