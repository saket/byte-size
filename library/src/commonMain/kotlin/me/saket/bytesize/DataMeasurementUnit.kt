package me.saket.bytesize

@PublishedApi
internal enum class DataMeasurementUnit(val bits: Int) {
  BinaryBytes(0b00),  // Base-2 bytes (KiB, MiB).
  DecimalBytes(0b01), // Base-10 bytes (KB, MB).
  BinaryBits(0b10),   // Base-2 bits (Kib, Mib).
  DecimalBits(0b11),  // Base-10 bits (Kb, Mb).
}
