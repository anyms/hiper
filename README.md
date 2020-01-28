[![](https://jitpack.io/v/anyms/hiper.svg)](https://jitpack.io/#anyms/hiper)
[![Build Status](https://travis-ci.org/anyms/hiper.svg?branch=master)](https://travis-ci.org/anyms/hiper)
[![License](https://img.shields.io/github/license/anyms/hiper.svg)](https://github.com/anyms/hiper/blob/master/LICENSE)

# Hiper - A Human Friendly HTTP Library for Android

> Hiper is for human by human.

# Getting Started

Add it in your root build.gradle at the end of repositories

```css
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```css
dependencies {
        implementation 'com.github.anyms:hiper:Tag'
}
```

# Using it so Simple.

Create a hiper instance.

```kotlin
val hiper = Hiper()
```

Now you are ready to see the power of hiper.

```kotlin
// simple GET request
hiper.get("http://httpbin.org/get")
    .ifFailed { response -> }
    .finally { response -> }

// simple POST request
hiper.post("http://httpbin.org/post")
    .ifFailed { response -> }
    .finally { response -> }

// sending parameters with your request
val params = hashMapOf(
    "name" to "Hiper",
    "age" to 1
)
hiper.get("http://httpbin.org/get")
    .addArgs(params)
    .ifFailed { response -> }
    .finally { response -> }
```

Download binary files

```kotlin
hiper.get("http://httpbin.org/get", isStream = true)
    .ifFailed { response -> }
    .ifStream { bytes ->
        if (bytes != null) {
            // writing bytes to a file
            ...
        } else {
            // closing the opened file
            ...
        }
    }
    .finally { response -> }
```


**Note**: Do not forget to call the `.finally {}` at the end of every request. It might looks like it doing everything async but it actually an emulation to give you an easy interface to work with, everything above the `.finally {}` block is a setup of your HTTP request. The `.finally {}` is actually sending the HTTP request.
