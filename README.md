# file-size

```groovy
implementation "me.saket.filesize:filesize:1.0.0"
```

```kotlin
val cacheSize = 512.megabytes
println(cacheSize)               // "512 MB"
println(cacheSize.bytes)         // "512000000"
println(cacheSize < 1.gigabytes) // "true"
println(cacheSize * 2)           // "1.02 GB"
```
