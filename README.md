# file-size [![Maven Central](https://img.shields.io/maven-central/v/me.saket.filesize/filesize?label=%20&color=success)](https://central.sonatype.com/artifact/me.saket.filesize/filesize) [![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/me.saket.filesize/filesize?label=%20&color=lightgrey&server=https%3A%2F%2Foss.sonatype.org%2F)](https://oss.sonatype.org/content/repositories/snapshots/me/saket/filesize/filesize/)

```groovy
implementation "me.saket.filesize:filesize:1.2.0"
```

```kotlin
val cacheSize = 512.megabytes
println(cacheSize.toString())          // "512 MB"
println(cacheSize.inWholeBytes)        // "512000000"
println(cacheSize + 88_000.kilobytes)  // "600 MB"
println(cacheSize * 2)                 // "1.02 GB"
println(cacheSize < 1.gigabytes)       // "true"
```
