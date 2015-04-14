package com.github.xolan.android.debouncer;

import android.util.Log;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicInteger;

public class DebouncerTest extends TestCase {

    private static final String TAG = "Debouncer";

    public class Counter {
        final AtomicInteger count;

        public Counter() {
            count = new AtomicInteger(0);
        }

        public void increment() {
            count.addAndGet(1);
            Log.i(TAG, "New value: " + asInt());
        }

        public void decrement() {
            count.decrementAndGet();
            Log.i(TAG, "New value: " + asInt());
        }

        public int asInt() {
            return count.intValue();
        }
    }

    /**
     * A counter is initialized and starts at 0.
     * Debounces 3 simulated "clicks" with 250 milliseconds margin.
     * Each runnable call increments the counter by 1.
     * After those 3 debounces, the counter should be exactly 1, meaning only 1 (the last) instance
     * of the runnable was actually ran.
     */
    public void testDebounce() {
        setName("testDebounce");

        final String identifier = "test";

        final Counter counter = new Counter();

        final Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, String.format("Runnable with identifier \"%s\" ran", identifier));
                counter.increment();
            }
        };

        // First "click"
        Log.i(TAG, "Debounce #1");
        Debouncer.debounce(identifier, r1, 250);
        try {
            Thread.sleep(100, 0);
            // Second "click" 100 millis later
            Log.i(TAG, "Debounce #2");
            Debouncer.debounce(identifier, r1, 250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100, 0);
            // Third "click" 100 millis later
            Log.i(TAG, "Debounce #3");
            Debouncer.debounce(identifier, r1, 250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(500, 0);
            assertFalse("Failed to debounce", counter.asInt() == 3);
            assertTrue("Failed to debounce", counter.asInt() == 1);
            Log.i(TAG, "Debouncer seems to work!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}