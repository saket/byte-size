package me.saket.filesize

import kotlin.math.roundToLong

@PublishedApi
internal actual fun Number.isDecimal(): Boolean = this is Double

internal actual fun Long.divideExact(other: Number): Long {
  return when (other) {
    is Byte -> this / other
    is Short -> this / other
    is Int -> this / other
    is Long -> this / other
    is Float -> (this / other).roundToLong()
    is Double -> (this / other).roundToLong()
    else -> error("Unsupported type: ${other::class}")
  }
}
