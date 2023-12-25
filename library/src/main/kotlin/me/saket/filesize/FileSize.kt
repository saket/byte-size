package me.saket.filesize

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
 * println(cacheSize < 1.gigabytes) // "true".
 *```
 */
@JvmInline
value class FileSize(val bytes: Long): Comparable<FileSize> {

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

  operator fun times(other: Int) =
    FileSize(bytes = this.bytes * other)

  operator fun times(other: Float) =
    FileSize(bytes = (this.bytes * other).roundToLong())

  operator fun times(other: Double) =
    FileSize(bytes = (this.bytes * other).roundToLong())

  operator fun div(other: FileSize) =
    FileSize(bytes = this.bytes / other.bytes)

  operator fun div(other: Int) =
    FileSize(bytes = this.bytes / other)

  operator fun div(other: Float) =
    FileSize(bytes = (this.bytes / other).roundToLong())

  operator fun div(other: Double) =
    FileSize(bytes = (this.bytes / other).roundToLong())

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

    inline val Int.bytes: FileSize
      get() = FileSize(bytes = this.toLong())

    inline val Long.bytes: FileSize
      get() = FileSize(bytes = this)

    inline val Int.kilobytes: FileSize
      get() = FileSize(bytes = this * BytesPerKb)

    inline val Double.kilobytes: FileSize
      get() = FileSize(bytes = (this * BytesPerKb).toLong())

    inline val Long.kilobytes: FileSize
      get() = FileSize(bytes = (this * BytesPerKb))

    inline val Int.megabytes: FileSize
      get() = FileSize(bytes = this * BytesPerMb)

    inline val Double.megabytes: FileSize
      get() = FileSize(bytes = (this * BytesPerMb).toLong())

    inline val Long.megabytes: FileSize
      get() = FileSize(bytes = (this * BytesPerMb))

    inline val Int.gigabytes: FileSize
      get() = FileSize(bytes = this * BytesPerGb)

    inline val Double.gigabytes: FileSize
      get() = FileSize(bytes = (this * BytesPerGb).toLong())

    inline val Long.gigabytes: FileSize
      get() = FileSize(bytes = (this * BytesPerGb))

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
