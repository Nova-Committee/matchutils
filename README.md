# matchutils

Universal Java library that adds a `Match` for more flexible, convenient and complex pattern matching.

### Adding this to your project

You can get the latest build via JitPack. To include this to your project, simply add this line to your `build.gradle`:

```groovy
repositories {
    // ...
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    // ...
    implementation "com.github.Nova-Committee:matchutils:main-SNAPSHOT"
}
```

### Usage

```java
SomeObject obj;
new Match<>(obj)
        .addEntry(obj1 -> {
            return obj1 instanceof AnotherObject; // The predicate of this entry
        }, obj1 -> {
            // Do something...
        })
        .addDefault(obj1 -> {
            // Do something else...
        })
        .matchAll();
```
