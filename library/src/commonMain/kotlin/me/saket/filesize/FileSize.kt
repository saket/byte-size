@file:Suppress("ConstPropertyName")

package me.saket.filesize

import dev.drewhamilton.poko.Poko
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * Represents a file size with byte-level precision.
 *
 * Example usage:
 *
 * ```
 * val cacheSize = 512.megabytes
 * println(cacheSize.toString())          // "512 MB"
 * println(cacheSize.inWholeBytes)        // "512000000"
 * println(cacheSize + 88_000.kilobytes)  // "600 MB"
 * println(cacheSize * 2)                 // "1.02 GB"
 * println(cacheSize < 1.gigabytes)       // "true"
 * ```
 */
@Poko
class FileSize(private val bytes: Long) : Comparable<FileSize> {

  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long
    get() = bytes

  @get:JvmName("inWholeKilobytes")
  val inWholeKilobytes: Long
    get() = bytes / BytesPerKb

  @get:JvmName("inWholeMegabytes")
  val inWholeMegabytes: Long
    get() = bytes / BytesPerMb

  @get:JvmName("inWholeGigabytes")
  val inWholeGigabytes: Long
    get() = bytes / BytesPerGb

  override operator fun compareTo(other: FileSize): Int =
    bytes.compareTo(other.bytes)

  operator fun plus(other: FileSize): FileSize =
    FileSize(bytes = this.bytes.addExact(other.bytes))

  operator fun minus(other: FileSize): FileSize =
    FileSize(bytes = this.bytes.subtractExact(other.bytes))

  operator fun times(other: FileSize): FileSize =
    this * other.bytes

  operator fun times(other: Number): FileSize {
    return FileSize(bytes = bytes.multiplyExact(other))
  }

  operator fun div(other: FileSize): FileSize =
    this / other.bytes

  operator fun div(other: Number): FileSize {
    return FileSize(bytes = bytes.divideExact(other))
  }

  override fun toString(): String {
    return when {
      bytes < 1_000 -> "${bytes.toStringAsFixed()} bytes"
      bytes < 1_000_000 -> "${(bytes / 1_000.0).toStringAsFixed()} KB"
      bytes < 1_000_000_000 -> "${(bytes / 1_000_000.0).toStringAsFixed()} MB"
      else -> "${(bytes / 1_000_000_000.0).toStringAsFixed()} GB"
    }
  }

  companion object {
    @PublishedApi internal const val BytesPerKb: Long = 1_000L
    @PublishedApi internal const val BytesPerMb: Long = 1_000L * BytesPerKb
    @PublishedApi internal const val BytesPerGb: Long = 1_000L * BytesPerMb

    @JvmStatic
    @get:JvmName("bytes")
    inline val Number.bytes: FileSize
      get() {
        if (this.isDecimal()) {
          // Double#bytes is already a compilation error. This runtime error prevents
          // developers from casting doubles as Numbers to bypass the compilation error.
          throw FileSizePrecisionException()
        }
        return FileSize(bytes = this.toLong())
      }

    @JvmStatic
    @get:JvmName("kilobytes")
    inline val Number.kilobytes: FileSize
      get() = FileSize(bytes = BytesPerKb) * this

    @JvmStatic
    @get:JvmName("megabytes")
    inline val Number.megabytes: FileSize
      get() = FileSize(bytes = BytesPerMb) * this

    @JvmStatic
    @get:JvmName("gigabytes")
    inline val Number.gigabytes: FileSize
      get() = FileSize(bytes = BytesPerGb) * this

    @get:JvmSynthetic
    @Deprecated(PrecisionLossErrorMessage, level = DeprecationLevel.ERROR)
    @Suppress("DeprecatedCallableAddReplaceWith")
    val Double.bytes: FileSize
      get() = throw FileSizePrecisionException()

    @PublishedApi internal const val PrecisionLossErrorMessage = "FileSize provides precision at the byte level. " +
      "Representing a fractional Double/Floa value as bytes may lead to precision loss. It is recommended to convert the " +
      "value to a whole number before using FileSize."
  }
}
