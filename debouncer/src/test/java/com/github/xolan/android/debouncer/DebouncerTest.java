package com.github.xolan.android.debouncer;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.atomic.AtomicInteger;

@Config(emulateSdk = 17)
@RunWith(RobolectricTestRunner.class)
public class DebouncerTest {

    private static final String TAG = "Debouncer";

    public class Counter {
        final AtomicInteger count;

        public Counter() {
            count = new AtomicInteger(0);

        }

        public void increment() {
            count.addAndGet(1);
        }

        public void decrement() {
            count.decrementAndGet();
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
    @Test
    public void testDebounce() {
        final String identifier = "test";

        final Counter counter = new Counter();

        final Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, String.format("Runnable with identifier \"%s\" ran", identifier));
                counter.increment();
            }
        };

        Robolectric.runUiThreadTasksIncludingDelayedTasks();

        // First "click"
        Debouncer.debounce(identifier, r1, 250);
        Robolectric.idleMainLooper(100);
        try {
            Thread.sleep(100, 0);
            // Second "click" 100 millis later
            Debouncer.debounce(identifier, r1, 250);
            Robolectric.idleMainLooper(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100, 0);
            // Third "click" 100 millis later
            Debouncer.debounce(identifier, r1, 250);
            Robolectric.idleMainLooper(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(500, 0);
            Robolectric.idleMainLooper(500);
            Assert.assertFalse("Failed to debounce. Counter was " + counter.asInt(), counter.asInt() == 3);
            Assert.assertTrue("Failed to debounce. Counter was " + counter.asInt() + ". Expected 1", counter.asInt() == 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}