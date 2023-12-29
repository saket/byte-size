package me.saket.filesize

import kotlin.math.pow

internal fun Long.toStringAsFixed(): String {
  return this.toDouble().toStringAsFixed()
}

internal fun Double.toStringAsFixed(): String {
  return this.toStringAsFixed(digits = 2).removeSuffix(".0")
}

/**
 * Copied from [compose-ui](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui-geometry/src/commonMain/kotlin/androidx/compose/ui/geometry/GeometryUtils.kt;drc=f246f23593ca89113a9023f61a32cdc7db09e99e).
 */
private fun Double.toStringAsFixed(digits: Int): String {
  val pow = 10f.pow(digits)
  val shifted = this * pow // shift the given value by the corresponding power of 10
  val decimal = shifted - shifted.toInt() // obtain the decimal of the shifted value
  // Manually round up if the decimal value is greater than or equal to 0.5f.
  // because kotlin.math.round(0.5f) rounds down
  val roundedShifted = if (decimal >= 0.5f) {
    shifted.toInt() + 1
  } else {
    shifted.toInt()
  }

  val rounded = roundedShifted / pow // divide off the corresponding power of 10 to shift back
  return if (digits > 0) {
    // If we have any decimal points, convert the float to a string
    rounded.toString()
  } else {
    // If we do not have any decimal points, return the int
    // based string representation
    rounded.toInt().toString()
  }
}

@PublishedApi
internal expect fun Number.isDecimal(): Boolean

internal expect fun Long.addExact(other: Long): Long

internal expect fun Long.subtractExact(other: Long): Long

internal expect fun Long.multiplyExact(other: Number): Long

internal expect fun Long.divideExact(other: Number): Long

