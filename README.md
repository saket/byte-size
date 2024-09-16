# byte-size

```groovy
implementation "me.saket.bytesize:bytesize:2.0.0-beta02"
```

```kotlin
val cacheSize = 512.megabytes
println(cacheSize.toString())          // "512 MB"
println(cacheSize.inWholeBytes)        // "512000000"
println(cacheSize + 88_000.kilobytes)  // "600 MB"
println(cacheSize * 2)                 // "1.02 GB"
println(cacheSize < 1.gigabytes)       // "true"
```

```kotlin
val perception = 2.gibibytes
val usable = 2.gigabytes
println("${perception - usable} lost on a 2GB drive") // "140.65 MiB lost on a 2GB drive"
```
