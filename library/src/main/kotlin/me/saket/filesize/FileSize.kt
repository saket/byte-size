package me.saket.filesize

import dev.drewhamilton.poko.Poko
import kotlin.math.roundToLong

/**
 * Represents a file size with byte-level precision.
 *
 * Example usage:
 *
 * ```
 * val cacheSize = 512.megabytes
 * println(cacheSize)  // "512 MB"
 * println(cacheSize.bytes) // "512000000"
 * println(cacheSize < 1.gigabytes) // "true"
 * println(cacheSize * 2)  // "1.02 GB"
 *```
 */
@Poko
class FileSize(val bytes: Long): Comparable<FileSize> {

  val inWholeKilobytes: Long
    get() = bytes / BytesPerKb

  val inWholeMegabytes: Long
    get() = bytes / BytesPerMb

  val inWholeGigabytes: Long
    get() = bytes / BytesPerGb

  override operator fun compareTo(other: FileSize): Int =
    bytes.compareTo(other.bytes)

  operator fun plus(other: FileSize) =
    FileSize(bytes = this.bytes + other.bytes)

  operator fun minus(other: FileSize) =
    FileSize(bytes = this.bytes - other.bytes)

  operator fun times(other: FileSize) =
    FileSize(bytes = this.bytes * other.bytes)

  operator fun times(other: Number): FileSize {
    val result = when (other) {
      is Byte -> bytes * other
      is Short -> bytes * other
      is Int -> bytes * other
      is Long -> bytes * other
      is Float -> (bytes * other).roundToLong()
      is Double -> (bytes * other).roundToLong()
      else -> error("Unsupported type: ${other::class.java}")
    }
    return FileSize(bytes = result)
  }

  operator fun div(other: FileSize) =
    FileSize(bytes = this.bytes / other.bytes)

  operator fun div(other: Number): FileSize {
    val result = when (other) {
      is Byte -> bytes / other
      is Short -> bytes / other
      is Int -> bytes / other
      is Long -> bytes / other
      is Float -> (bytes / other).roundToLong()
      is Double -> (bytes / other).roundToLong()
      else -> error("Unsupported type: ${other::class.java}")
    }
    return FileSize(bytes = result)
  }

  override fun toString(): String {
    return when {
      bytes < 1_000 -> "${bytes.toStringAsFixed()} bytes"
      bytes < 1_000_000 -> "${(bytes / 1_000.0).toStringAsFixed()} KB"
      bytes < 1_000_000_000 -> "${(bytes / 1_000_000.0).toStringAsFixed()} MB"
      else -> "${(bytes / 1_000_000_000.0).toStringAsFixed()} GB"
    }
  }

  @Suppress("ConstPropertyName")
  companion object {
    @PublishedApi internal const val BytesPerKb: Long = 1_000L
    @PublishedApi internal const val BytesPerMb: Long = 1_000L * BytesPerKb
    @PublishedApi internal const val BytesPerGb: Long = 1_000L * BytesPerMb

    inline val Number.bytes: FileSize
      get() = FileSize(bytes = this.toLong())

    inline val Number.kilobytes: FileSize
      get() = FileSize(bytes = BytesPerKb) * this

    inline val Number.megabytes: FileSize
      get() = FileSize(bytes = BytesPerMb) * this

    inline val Number.gigabytes: FileSize
      get() = FileSize(bytes = BytesPerGb) * this

    @Deprecated(
      message = "FileSize provides precision at the byte level. Representing a fractional Double value as bytes may " +
        "lead to precision loss. It is recommended to convert the value to a whole number before using FileSize.",
      level = DeprecationLevel.ERROR,
    )
    @Suppress("DeprecatedCallableAddReplaceWith")
    val Double.bytes: FileSize
      get() = error("unreachable")
  }
}
