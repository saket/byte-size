package me.saket.filesize

import java.math.BigDecimal

internal actual fun Long.addExact(other: Long): Long = Math.addExact(this, other)

internal actual fun Long.subtractExact(other: Long): Long = Math.subtractExact(this, other)

internal actual fun Long.multiplyExact(other: Number): Long {
  return when (other) {
    is Byte, is Short, is Int, is Long -> Math.multiplyExact(this, other.toLong())
    else -> {
      val otherAsBigDecimal = when (other) {
        is Float -> BigDecimal(other.toString())
        is Double -> BigDecimal.valueOf(other)
        else -> error("Unsupported type: ${other::class.java}")
      }
      BigDecimal.valueOf(this)
        .multiply(otherAsBigDecimal)
        .toBigInteger()
        .longValueExact()
    }
  }
}
