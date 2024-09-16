package me.saket.bytesize

import assertk.assertThat
import assertk.assertions.isNull
import kotlin.reflect.full.companionObject
import kotlin.test.Test

class CompanionObjectTest {
  @Test fun verify_no_companion_objects() {
    // Addition of a companion object significantly increases
    // the number of instructions at call sites in a value class.
    // https://github.com/saket/byte-size/issues/13
    for (clazz in (ByteSize::class.sealedSubclasses + ByteSize::class)) {
      assertThat(clazz.companionObject).isNull()
    }
  }
}
