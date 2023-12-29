package me.saket.filesize

import kotlin.math.abs
import platform.Foundation.NSDecimalNumber

internal actual fun Long.addExact(other: Long): Long {
  val result = this + other

  if ((this xor result) and (other xor result) < 0) {
    throw ArithmeticException("long overflow")
  }
  return result
}

internal actual fun Long.subtractExact(other: Long): Long {
  val result = this - other

  if ((this xor other) and (this xor result) < 0) {
    throw ArithmeticException("long overflow")
  }
  return result
}

internal actual fun Long.multiplyExact(other: Number): Long {
  return when (other) {
    is Byte, is Short, is Int, is Long -> {
      val otherAsLong = other.toLong()
      val result = this * otherAsLong
      val absA = abs(this)
      val absB = abs(otherAsLong)
      if ((absA or absB) ushr 31 != 0L) {
        if (((this != 0L) && (result / otherAsLong != this)) || (this == Long.MIN_VALUE && otherAsLong == -1L)) {
          throw ArithmeticException("long overflow")
        }
      }
      result
    }
    is Float, is Double -> {
      NSDecimalNumber(long = this)
        .decimalNumberByMultiplyingBy(NSDecimalNumber(string = other.toString()))
        .integerValue()
        .also { result ->
          if (result < 0 && this > 0 && ((other is Double && other > 0) || (other is Float && other > 0))) {
            throw ArithmeticException("long overflow")
          }
        }
    }
    else -> error("Unsupported type: ${other::class}")
  }
}
