package me.saket.bytesize

import assertk.Assert
import assertk.assertions.isEqualTo

internal fun Assert<ByteSize>.isApproximatelyEqualTo(other: ByteSize) {
  // todo: find a better way that does not involve comparing their formatted strings
  this.transform { it.toString() }
    .isEqualTo(other.toString())
}
