# android-debouncer

[![Build Status](https://travis-ci.org/xolan/android-debouncer.svg?branch=master)](https://travis-ci.org/xolan/android-debouncer)

## Usage

```java
...
Debouncer.debounce("something", new Runnable() {
    @Override
    public void run() {
        // Will not be ran due to de call below
        ...
    }
}, 250);

Thread.sleep(100, 0);

Debouncer.debounce("something", new Runnable() {
    @Override
    public void run() {
        // Overrides the previous call with identifier "something", running after approx. 350ms.
        // 100ms simulated "wait" time, 250ms debounce time.
        ...
    }
}, 250);
...
```