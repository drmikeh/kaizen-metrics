package com.kaizencreek.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 10/30/13
 */
public class Counter extends AbstractMetric implements Metric {

    private final AtomicLong count;

    Counter(MetricName metricName) {
        super(metricName);
        this.count = new AtomicLong(0);
    }

    /**
     * Increment the counter by one.
     */
    public void inc() {
        inc(1);
    }

    /**
     * Increment the counter by {@code n}.
     *
     * @param n  the amount by which the counter will be increased
     */
    public void inc(long n) {
        count.addAndGet(n);
    }

    /**
     * Decrement the counter by one.
     */
    public void dec() {
        dec(1);
    }

    /**
     * Decrement the counter by {@code n}.
     *
     * @param n  the amount by which the counter will be decreased
     */
    public void dec(long n) {
        count.addAndGet(0 - n);
    }

    /**
     * Returns the counter's current value.
     *
     * @return the counter's current value
     */
    public long count() {
        return count.get();
    }

    /**
     * Resets the counter to 0.
     */
    @Override
    public void reset() {
        count.set(0);
    }

    @Override
    public String toString() {
        return String.format("%s : count = %,d", metricName, count());
    }
}