package me.saket.bytesize

import kotlin.math.pow

internal fun Long.toStringAsFixed(): String {
  return this.toDouble().toStringAsFixed()
}

internal fun Double.toStringAsFixed(): String {
  return this.toStringAsFixed(digits = 2).removeSuffix(".0")
}

/**
 * Adapted from [compose-ui](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui-geometry/src/commonMain/kotlin/androidx/compose/ui/geometry/GeometryUtils.kt;drc=f246f23593ca89113a9023f61a32cdc7db09e99e).
 */
private fun Double.toStringAsFixed(digits: Int): String {
  val pow = 10f.pow(digits)
  val shifted = this * pow // shift the given value by the corresponding power of 10
  val decimal = shifted - shifted.toLong() // obtain the decimal of the shifted value
  // Manually round up if the decimal value is greater than or equal to 0.5f.
  // because kotlin.math.round(0.5f) rounds down
  val roundedShifted = if (decimal >= 0.5f) {
    shifted.toLong() + 1
  } else {
    shifted.toLong()
  }

  val rounded = roundedShifted / pow // divide off the corresponding power of 10 to shift back
  return if (digits > 0) {
    // If we have any decimal points, convert the float to a string
    rounded.toString()
  } else {
    // If we do not have any decimal points, return the long
    // based string representation
    rounded.toLong().toString()
  }
}

@PublishedApi
internal fun Long.timesExact(other: Double): Double {
  return (this * other).also {
    if (it.isInfinite()) {
      throw ArithmeticException("Multiplication resulted in overflow")
    }
  }
}

@PublishedApi
internal fun Double.toLongOrThrow(): Long {
  if (isNaN() || isInfinite()) {
    throw ArithmeticException("Cannot convert $this to Long")
  } else if (this < Long.MIN_VALUE || this > Long.MAX_VALUE) {
    throw ArithmeticException("Double value out of Long range: $this")
  } else {
    return toLong()
  }
}
