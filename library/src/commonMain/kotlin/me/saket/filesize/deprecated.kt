package me.saket.filesize

@Deprecated(
  message = "Renamed to ByteSize",
  replaceWith = ReplaceWith("ByteSize", "me.saket.bytesize.ByteSize"),
  level = DeprecationLevel.ERROR,
)
@Suppress("DeprecatedCallableAddReplaceWith", "DEPRECATION_ERROR")
class FileSize constructor(val bytes: Long) : Comparable<FileSize> {

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeBytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeKilobytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeMegabytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeGigabytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  override operator fun compareTo(other: FileSize): Int = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun plus(other: FileSize): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun minus(other: FileSize): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun times(other: Number): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun div(other: FileSize): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun div(other: Number): Nothing = error("unreachable")

  @Deprecated(
    message = "FileSize has been renamed to ByteSize.",
    level = DeprecationLevel.ERROR,
  )
  companion object {
    @Deprecated(
      message = "FileSize has been renamed to ByteSize.",
      replaceWith = ReplaceWith("this.decimalBytes", "me.saket.bytesize.decimalBytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.bytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports to allow the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.kilobytes", "me.saket.bytesize.kilobytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.kilobytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports to allow the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.megabytes", "me.saket.bytesize.megabytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.megabytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports to allow the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.gigabytes", "me.saket.bytesize.gigabytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.gigabytes: Nothing get() = error("unreachable")
  }
}
