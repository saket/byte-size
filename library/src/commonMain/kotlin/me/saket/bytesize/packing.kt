package me.saket.bytesize

/**
 * Packs byte size and its measurement unit into a single `Long` by using the
 * first 62 bits for storing the size and the last 2 for representing its
 * [DataMeasurementUnit].
 */
@PublishedApi
internal inline fun packValue(bytes: Long, unit: DataMeasurementUnit): Long {
  return (unit.bits.toLong() shl 62) or (bytes and 0x3FFFFFFFFFFFFFFF)
}

@PublishedApi
internal inline fun unpackBytesValue(packedValue: Long): Long {
  return packedValue and 0x3FFFFFFFFFFFFFFF
}

@PublishedApi
internal inline fun unpackMeasurementUnit(packedValue: Long): DataMeasurementUnit {
  return when (val bits = ((packedValue shr 62) and 0b11).toInt()) {
    0b00 -> DataMeasurementUnit.BinaryBytes
    0b01 -> DataMeasurementUnit.DecimalBytes
    0b10 -> DataMeasurementUnit.BinaryBits
    0b11 -> DataMeasurementUnit.DecimalBits
    else -> error("Unknown measurement unit: $bits")
  }
}
