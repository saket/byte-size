package me.saket.bytesize

import dev.erikchristensen.javamath2kmp.minusExact
import dev.erikchristensen.javamath2kmp.plusExact
import dev.erikchristensen.javamath2kmp.timesExact
import kotlin.math.roundToLong

@PublishedApi
internal inline fun ByteSize.commonPlus(other: ByteSize): Long {
  return inWholeBytes.plusExact(other.inWholeBytes)
}

@PublishedApi
internal inline fun ByteSize.commonMinus(other: ByteSize): Long {
  return inWholeBytes.minusExact(other.inWholeBytes)
}

@PublishedApi
internal inline fun ByteSize.commonDiv(other: ByteSize): Double {
  return this.inWholeBytes.toDouble() / other.inWholeBytes
}

@PublishedApi
internal inline fun ByteSize.commonDiv(other: Number): Long {
  return when (other) {
    is Byte -> inWholeBytes / other
    is Short -> inWholeBytes / other
    is Int -> inWholeBytes / other
    is Long -> inWholeBytes / other
    is Float -> (inWholeBytes / other).roundToLong()
    is Double -> (inWholeBytes / other).roundToLong()
    else -> error("Unsupported number: ${other::class}")
  }
}

@PublishedApi
internal inline fun ByteSize.commonTimes(other: Number): Long {
  return when (other) {
    is Byte,
    is Short,
    is Int,
    is Long,
    -> inWholeBytes.timesExact(other.toLong())
    is Float -> inWholeBytes.timesExact(other.toDouble()).toLongOrThrow()
    is Double -> inWholeBytes.timesExact(other).toLongOrThrow()
    else -> error("Unsupported number: ${other::class}")
  }
}

@PublishedApi
internal inline fun ByteSize.commonCompareTo(other: ByteSize): Int {
  return inWholeBytes.compareTo(other.inWholeBytes)
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

@PublishedApi
internal fun Number.hasFractionalPart(): Boolean {
  return when (this) {
    is Float -> this % 1 != 0f
    is Double -> this % 1 != 0.0
    else -> false
  }
}
