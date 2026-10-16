package me.saket.bytesize.internal

import kotlin.math.abs
import kotlin.math.pow

/**
 * Formats [size], a count of the smallest of [units], in the largest unit it still reaches
 * once rounded half up to two decimals, so that 999,995 bytes is "1 MB" rather than
 * "1000 KB". Each unit is [unitStep] times the previous one.
 *
 * This is integer arithmetic so that the rounding is exact. 1.005 has no exact [Double]
 * representation, so rounding 1,005 bytes through one prints "1 KB" rather than "1.01 KB".
 *
 * Unlike the `toString()` overrides that call it, this isn't inline, so that a fix here
 * reaches code compiled against an earlier version of this library.
 */
@PublishedApi
internal fun formatSize(size: Long, unitStep: Long, vararg units: String): String {
  if (size > -unitStep && size < unitStep) {
    return "$size ${units[0]}"
  }
  var unitIndex = 1
  var unit = unitStep
  var hundredths = roundToHundredths(size, unit)
  while (hundredths >= unitStep * 100 && unitIndex < units.lastIndex) {
    unitIndex++
    unit *= unitStep
    hundredths = roundToHundredths(size, unit)
  }
  val sign = if (size < 0) "-" else ""
  val whole = hundredths / 100
  val fraction = hundredths % 100
  val decimals = when {
    fraction == 0L -> ""
    fraction % 10 == 0L -> ".${fraction / 10}"
    fraction < 10 -> ".0$fraction"
    else -> ".$fraction"
  }
  return "$sign$whole$decimals ${units[unitIndex]}"
}

/**
 * Returns the magnitude of [size] / [unit] rounded half up to two decimals, in hundredths.
 *
 * Taking the magnitude of the quotient and the remainder rather than of [size] is what makes
 * [Long.MIN_VALUE] work, as its own magnitude doesn't fit in a [Long]. `remainder * 100` can't
 * overflow while [unit] stays below 2^56, and the largest unit, a pebibyte, is 2^50.
 */
private fun roundToHundredths(size: Long, unit: Long): Long {
  val whole = abs(size / unit)
  val remainder = abs(size % unit)
  return whole * 100 + (remainder * 100 + unit / 2) / unit
}

// The toStringAsFixed() functions below aren't called by this library any more, but code
// compiled against byte-size 2.3.0 or earlier inlined toString() bodies that call them, so
// removing them would break that code at runtime.

@PublishedApi
internal fun Long.toStringAsFixed(): String {
  return this.toDouble().toStringAsFixed()
}

@PublishedApi
internal fun Double.toStringAsFixed(): String {
  return this.toStringAsFixed(digits = 2).removeSuffix(".0")
}

/**
 * Adapted from [compose-ui](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui-geometry/src/commonMain/kotlin/androidx/compose/ui/geometry/GeometryUtils.kt;drc=f246f23593ca89113a9023f61a32cdc7db09e99e).
 */
@PublishedApi
internal inline fun Double.toStringAsFixed(digits: Int): String {
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
