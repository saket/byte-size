package me.saket.bytesize.internal

import dev.erikchristensen.javamath2kmp.minusExact
import dev.erikchristensen.javamath2kmp.plusExact
import dev.erikchristensen.javamath2kmp.timesExact
import kotlin.math.roundToLong
import me.saket.bytesize.BitPrecision
import me.saket.bytesize.BytePrecision
import me.saket.bytesize.ByteSize

@PublishedApi
internal inline val BitsPerByte: Long get() = 8

@PublishedApi
internal inline fun BytePrecision.commonPlus(other: ByteSize): Long {
  return inWholeBytes.plusExact(other.inWholeBytes)
}

@PublishedApi
internal inline fun BitPrecision.commonPlus(other: ByteSize): Long {
  return inWholeBits.plusExact(other.inWholeBits())
}

@PublishedApi
internal inline fun BytePrecision.commonMinus(other: ByteSize): Long {
  return inWholeBytes.minusExact(other.inWholeBytes)
}

@PublishedApi
internal inline fun BitPrecision.commonMinus(other: ByteSize): Long {
  return inWholeBits.minusExact(other.inWholeBits())
}

@PublishedApi
internal inline fun BytePrecision.commonDiv(other: ByteSize): Double {
  return this.inWholeBytes.toDouble() / other.inWholeBytes
}

@PublishedApi
internal inline fun BitPrecision.commonDiv(other: ByteSize): Double {
  return this.inWholeBits.toDouble() / other.inWholeBits()
}

@PublishedApi
internal inline fun BytePrecision.commonDiv(other: Number): Long {
  return when (other) {
    is Byte,
    is Short,
    is Int,
    is Long -> inWholeBytes / other.toLong()
    is Float -> (inWholeBytes / other).roundToLong()
    is Double -> (inWholeBytes / other).roundToLong()
    else -> error("Unsupported number: ${other::class}")
  }
}

@PublishedApi
internal inline fun BitPrecision.commonDiv(other: Number): Long {
  return when (other) {
    is Byte,
    is Short,
    is Int,
    is Long -> inWholeBits / other.toLong()
    is Float -> (inWholeBits / other).roundToLong()
    is Double -> (inWholeBits / other).roundToLong()
    else -> error("Unsupported number: ${other::class}")
  }
}

@PublishedApi
internal inline fun BytePrecision.commonTimes(other: Number): Long {
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
internal inline fun BitPrecision.commonTimes(other: Number): Long {
  return when (other) {
    is Byte,
    is Short,
    is Int,
    is Long,
    -> inWholeBits.timesExact(other.toLong())
    is Float -> inWholeBits.timesExact(other.toDouble()).toLongOrThrow()
    is Double -> inWholeBits.timesExact(other).toLongOrThrow()
    else -> error("Unsupported number: ${other::class}")
  }
}

@PublishedApi
internal inline fun BytePrecision.commonCompareTo(other: ByteSize): Int {
  return inWholeBytes.compareTo(other.inWholeBytes)
}

@PublishedApi
internal inline fun BitPrecision.commonCompareTo(other: ByteSize): Int {
  return inWholeBits.compareTo(other.inWholeBits())
}

@PublishedApi
internal inline fun ByteSize.inWholeBits(): Long {
  return when (this) {
    is BitPrecision -> inWholeBits
    is BytePrecision -> inWholeBytes * BitsPerByte
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

@PublishedApi
internal fun Number.hasFractionalPart(): Boolean {
  return when (this) {
    is Float -> this % 1 != 0f
    is Double -> this % 1 != 0.0
    else -> false
  }
}
