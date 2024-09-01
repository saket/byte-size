package me.saket.bytesize

/**
 * Packs byte size and its storage unit into a single `Long` by using the
 * first 62 bits for storing the size and the last 2 for representing its
 * [DataStorageUnit].
 */
@PublishedApi
internal inline fun packValue(bytes: Long, unit: DataStorageUnit): Long {
  return (unit.bits.toLong() shl 62) or (bytes and 0x3FFFFFFFFFFFFFFF)
}

@PublishedApi
internal inline fun unpackBytesValue(packedValue: Long): Long {
  return packedValue and 0x3FFFFFFFFFFFFFFF
}

@PublishedApi
internal inline fun unpackStorageUnit(packedValue: Long): DataStorageUnit {
  return when (val bits = ((packedValue shr 62) and 0b11).toInt()) {
    0b00 -> DataStorageUnit.BinaryBytes
    0b01 -> DataStorageUnit.DecimalBytes
    0b10 -> DataStorageUnit.BinaryBits
    0b11 -> DataStorageUnit.DecimalBits
    else -> error("Unknown storage unit: $bits")
  }
}
