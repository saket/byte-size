package me.saket.bytesize

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

/**
 * A `value class` is only kept unboxed while its static type _is_ the value class. Every operator
 * declared on [ByteSize] necessarily accepts the sealed interface, so passing a size to one boxes
 * it — and the internal helpers those operators delegate to are extensions on an interface too, so
 * the receiver boxes as well. Two allocations to perform one addition.
 *
 * The same-precision operator overloads and the [Int]/[Long] receiver overloads of the unit
 * properties exist to avoid that. This test guards them by checking compiled call sites for
 * `box-impl`, the static factory Kotlin generates to box a value class. That name is written to a
 * class file's constant pool if and only if the class boxes a value class somewhere, so its
 * absence is proof that a call site allocates nothing.
 *
 * See https://github.com/saket/byte-size/issues/13 for the related companion object problem.
 */
class AllocationFreeCallSitesTest {

  @Test fun same_precision_operators_and_primitive_receivers_do_not_box() {
    assertThat(boxesAValueClass<AllocationFree>()).isFalse()
  }

  /**
   * Guards the test above: if boxing detection ever stops working, this fails too rather than
   * letting [AllocationFree] pass vacuously. Mixing precisions has to go through [ByteSize], so
   * these call sites are expected to box.
   */
  @Test fun mixing_precisions_still_boxes() {
    assertThat(boxesAValueClass<MixedPrecision>()).isTrue()
  }

  private inline fun <reified T : Any> boxesAValueClass(): Boolean {
    val resource = T::class.java.name.replace('.', '/') + ".class"
    val bytecode = checkNotNull(T::class.java.classLoader.getResourceAsStream(resource)) {
      "Cannot find $resource on the test classpath"
    }.use { it.readBytes() }
    return bytecode.containsUtf8("box-impl")
  }

  private fun ByteArray.containsUtf8(text: String): Boolean {
    val needle = text.encodeToByteArray()
    return (0..size - needle.size).any { start ->
      needle.indices.all { this[start + it] == needle[it] }
    }
  }

  /**
   * Call sites that must compile down to primitive arithmetic. Deliberately free of assertions and
   * of anything else that could box a value class, because the check is on the whole class file.
   */
  @Suppress("unused")
  object AllocationFree {
    // The pattern that motivated these overloads: accumulate sizes over a hot loop.
    fun sumDecimal(sizes: LongArray): DecimalByteSize {
      var total = DecimalByteSize(0L)
      for (size in sizes) total += size.decimalBytes
      return total
    }

    fun sumBinary(sizes: IntArray): BinaryByteSize {
      var total = BinaryByteSize(0L)
      for (size in sizes) total += size.binaryBytes
      return total
    }

    fun sumBits(sizes: IntArray): DecimalBitSize {
      var total = DecimalBitSize(0L)
      for (size in sizes) total += size.decimalBits
      return total
    }

    fun decimalOperators(a: DecimalByteSize, b: DecimalByteSize): Double =
      if (a > b) (a - b) / b else (a + b) / b

    fun binaryOperators(a: BinaryByteSize, b: BinaryByteSize): Double =
      if (a > b) (a - b) / b else (a + b) / b

    fun bitOperators(a: DecimalBitSize, b: DecimalBitSize): Double =
      if (a > b) (a - b) / b else (a + b) / b

    fun fromIntReceivers(n: Int): Long =
      n.decimalBytes.inWholeBytes + n.kilobytes.inWholeBytes + n.megabytes.inWholeBytes +
        n.gigabytes.inWholeBytes + n.binaryBytes.inWholeBytes + n.kibibytes.inWholeBytes +
        n.mebibytes.inWholeBytes + n.gibibytes.inWholeBytes + n.decimalBits.inWholeBits +
        n.kilobits.inWholeBits + n.megabits.inWholeBits + n.gigabits.inWholeBits

    fun fromLongReceivers(n: Long): Long =
      n.decimalBytes.inWholeBytes + n.kilobytes.inWholeBytes + n.megabytes.inWholeBytes +
        n.gigabytes.inWholeBytes + n.binaryBytes.inWholeBytes + n.kibibytes.inWholeBytes +
        n.mebibytes.inWholeBytes + n.gibibytes.inWholeBytes + n.decimalBits.inWholeBits +
        n.kilobits.inWholeBits + n.megabits.inWholeBits + n.gigabits.inWholeBits
  }

  @Suppress("unused")
  object MixedPrecision {
    fun add(a: DecimalByteSize, b: BinaryByteSize): ByteSize = a + b
  }
}
