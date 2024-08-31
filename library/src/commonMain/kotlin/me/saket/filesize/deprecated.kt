package me.saket.filesize

@Deprecated(
  message = "Renamed to ByteSize",
  replaceWith = ReplaceWith("ByteSize", "me.saket.bytesize.ByteSize"),
  level = DeprecationLevel.ERROR,
)
@Suppress("DeprecatedCallableAddReplaceWith")
class FileSize constructor(val bytes: Long) : Comparable<Any> {

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeBytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeKilobytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeMegabytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  val inWholeGigabytes: Long get() = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  override operator fun compareTo(other: Any): Int = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun plus(other: Any): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun minus(other: Any): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun times(other: Any): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun times(other: Number): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun div(other: Any): Nothing = error("unreachable")

  @Deprecated("FileSize has been renamed to ByteSize", level = DeprecationLevel.ERROR)
  operator fun div(other: Number): Nothing = error("unreachable")

  @Deprecated(
    message = "FileSize has been renamed to ByteSize.",
    level = DeprecationLevel.ERROR,
  )
  companion object {
    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports for the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.bytes", "me.saket.filesize.bytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.bytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports for the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.bytes", "me.saket.filesize.bytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.kilobytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports for the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.bytes", "me.saket.filesize.bytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.megabytes: Nothing get() = error("unreachable")

    @Deprecated(
      message = "FileSize has been renamed to ByteSize. Please delete the existing imports for the IDE to suggest the new ones.",
      replaceWith = ReplaceWith("this.bytes", "me.saket.filesize.bytes"),
      level = DeprecationLevel.ERROR,
    )
    inline val Number.gigabytes: Nothing get() = error("unreachable")
  }
}
