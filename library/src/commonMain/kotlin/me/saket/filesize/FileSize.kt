@file:Suppress("ConstPropertyName")

package me.saket.filesize

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.drewhamilton.poko.Poko
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

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
class FileSize(private val bytes: BigDecimal) : Comparable<FileSize> {

  constructor(bytes: Long): this(BigDecimal.fromLong(bytes))

  @get:JvmName("inWholeBytes")
  val inWholeBytes: Long
    get() = bytes.longValue(exactRequired = true)

  @get:JvmName("inWholeKilobytes")
  val inWholeKilobytes: Long
    get() = inWholeBytes / BytesPerKb

  @get:JvmName("inWholeMegabytes")
  val inWholeMegabytes: Long
    get() = inWholeBytes / BytesPerMb

  @get:JvmName("inWholeGigabytes")
  val inWholeGigabytes: Long
    get() = inWholeBytes / BytesPerGb

  override operator fun compareTo(other: FileSize): Int =
    bytes.compareTo(other.bytes)

  operator fun plus(other: FileSize): FileSize =
    FileSize(bytes = bytes + other.bytes)

  operator fun minus(other: FileSize): FileSize =
    FileSize(bytes = bytes - other.bytes)

  operator fun times(other: FileSize): FileSize =
    this * other.inWholeBytes

  operator fun times(other: Number): FileSize =
    FileSize(bytes = bytes * BigNumber(other))

  operator fun div(other: FileSize): FileSize =
    this / other.inWholeBytes

  operator fun div(other: Number): FileSize =
    FileSize(bytes = bytes / BigNumber(other))

  override fun toString(): String {
    return when {
      bytes < 1_000 -> "${inWholeBytes.toStringAsFixed()} bytes"
      bytes < 1_000_000 -> "${(inWholeBytes / 1_000.0).toStringAsFixed()} KB"
      bytes < 1_000_000_000 -> "${(inWholeBytes / 1_000_000.0).toStringAsFixed()} MB"
      else -> "${(inWholeBytes / 1_000_000_000.0).toStringAsFixed()} GB"
    }
  }

  companion object {
    @PublishedApi internal const val BytesPerKb: Long = 1_000L
    @PublishedApi internal const val BytesPerMb: Long = 1_000L * BytesPerKb
    @PublishedApi internal const val BytesPerGb: Long = 1_000L * BytesPerMb

    @JvmStatic
    @get:JvmName("bytes")
    inline val Number.bytes: FileSize
      get() = FileSize(BigNumber(this))

    @JvmStatic
    @get:JvmName("kilobytes")
    inline val Number.kilobytes: FileSize
      get() = FileSize(BigNumber(this) * BytesPerKb)

    @JvmStatic
    @get:JvmName("megabytes")
    inline val Number.megabytes: FileSize
      get() = FileSize(BigNumber(this) * BytesPerMb)

    @JvmStatic
    @get:JvmName("gigabytes")
    inline val Number.gigabytes: FileSize
      get() = FileSize(BigNumber(this) * BytesPerGb)
  }
}
