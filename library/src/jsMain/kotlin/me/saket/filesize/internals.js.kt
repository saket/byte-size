package me.saket.filesize

@PublishedApi
@Suppress("UNUSED_VARIABLE")
internal actual fun Number.isDecimal(): Boolean {
  val thisNum = this //js() can only accept const strings; create a temporary variable to reference `this`
  return js("thisNum % 1 != 0").unsafeCast<Boolean>()
}

internal actual fun Long.addExact(other: Long): Long {
  return BigDecimal(this.toString())
    .add(BigDecimal(other))
    .toLong()
}
internal actual fun Long.subtractExact(other: Long): Long {
  return BigDecimal(this.toString())
    .subtract(BigDecimal(other))
    .toLong()
}

internal actual fun Long.multiplyExact(other: Number): Long {
  return BigDecimal(this)
    .multiply(BigDecimal(other))
    .toLong()
}

internal actual fun Long.divideExact(other: Number): Long {
  return BigDecimal(this.toString())
    .divide(BigDecimal(other))
    .toLong()
}


@JsModule("js-big-decimal")
@JsNonModule
@JsName("bigDecimal")
private external class BigDecimal(value: dynamic) {

  fun add(number: dynamic): BigDecimal

  fun subtract(number: dynamic): BigDecimal

  fun multiply(number: dynamic): BigDecimal

  fun divide(number: dynamic): BigDecimal

  fun round(): BigDecimal

  fun getValue(): String

}

private fun BigDecimal.toLong(): Long {
  return try {
    round().getValue().toLong()
  } catch (e: NumberFormatException) {
    throw ArithmeticException(e.message)
  }
}
