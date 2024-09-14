package me.saket.bytesize

import kotlin.jvm.JvmSynthetic

@PublishedApi
internal sealed interface BytePrecision {
  @get:JvmSynthetic
  val inWholeBytes: Long
}

@PublishedApi
internal sealed interface BitPrecision {
  @get:JvmSynthetic
  val inWholeBits: Long
}
