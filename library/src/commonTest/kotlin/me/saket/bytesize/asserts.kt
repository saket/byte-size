package me.saket.bytesize

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.toStringFun

internal fun Assert<ByteSize>.isApproximatelyEqualTo(other: ByteSize) {
  // todo: find a better way that does not involve comparing their formatted strings
  toStringFun().isEqualTo(other.toString())
}
