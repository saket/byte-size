# file-size

```groovy
implementation "me.saket.filesize:filesize:1.1.0"
```

```kotlin
val cacheSize = 512.megabytes
println(cacheSize.toString())          // "512 MB"
println(cacheSize.inWholeBytes)        // "512000000"
println(cacheSize + 88_000.kilobytes)  // "600 MB"
println(cacheSize * 2)                 // "1.02 GB"
println(cacheSize < 1.gigabytes)       // "true"
```
