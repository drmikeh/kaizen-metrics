package com.kaizencreek.metrics;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * <code>SimpleTimer</code> is a timer that measures the elapsed time from the
 * instant that start() is called until the instant that end() is called.
 */
public class SimpleTimer {

    private long startTime = 0;
    private long endTime = 0;

    private static final PeriodFormatter formatter =
            new PeriodFormatterBuilder()
                    .appendDays().appendSuffix("d")
                    .appendHours().appendSuffix("h")
                    .appendMinutes().appendSuffix("m")
                    .appendSeconds().appendSuffix("s")
                    .appendMillis().appendSuffix("ms")
                    .toFormatter();

    /**
     * Constructs a SimpleTimer.
     */
    public SimpleTimer() {
    }

    /**
     * Starts the SimpleTimer.
     *
     * @return  The SimpleTimer.
     */
    public SimpleTimer start() {
        reset();
        startTime = System.currentTimeMillis();
        return this;
    }

    /**
     * Ends the SimpleTimer.
     *
     * @return  The SimpleTimer.
     */
    public SimpleTimer end() {
        endTime = System.currentTimeMillis();
        return this;
    }

    /**
     * Resets the SimpleTimer.
     *
     * @return  The SimpleTimer.
     */
    public SimpleTimer reset() {
        startTime = endTime = 0;
        return this;
    }

    /**
     * Returns the start time of the SimpleTimer in milliseconds.
     *
     * @return  The start time in milliseconds.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the SimpleTimer in milliseconds.
     *
     * @return  The end time in milliseconds.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Returns the elapsed time of the SimpleTimer in milliseconds.
     * <p>
     * If the timer has not been started, zero is returned.
     * If the timer has been started but is still running, the elapsed time is
     * calculated as the current system time minus the start time.
     *
     * @return  The elapsed time in milliseconds.
     */
    public long getElapsedTimeMillis() {
        if (startTime == 0) {
            return 0;
        }
        if (endTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    /**
     * Returns an estimation of the expected time (duration) left until completion.
     * Note that this is a pretty simple / naive algorithm that does not
     * consider the throughput changing over time (i.e. recent throughput may
     * be much greater or lesser than earlier throughput).
     *
     * @param processed  the number of items that have been processed
     * @param total  the total number of items to be processed
     * @return  The estimated duration remaining until processing of
     *          <code>total</code> items is complete.
     */
    public Duration getETA(int processed, int total) {
        if (processed == 0) {
            return new Duration(0);    // any guess will do.
        }

        double throughput = processed / (double) getElapsedTimeMillis();
        long millis = (long) ((total - processed) / throughput);
        Duration eta = new Duration(millis);
        return eta;
    }

    /**
     * Returns an estimation of the time when processing will complete.
     *
     * @param processed  the number of items that have been processed
     * @param total  the total number of items to be processed
     * @return  The estimated time when the processing of <code>total</code> items is complete.
     */
    public DateTime getETATime(int processed, int total) {
        Duration eta = getETA(processed, total);
        DateTime etaTime = new DateTime().plus(eta);
        return etaTime;
    }

    /**
     * Returns the elapsed time as a <code>Duration</code>
     *
     * @return  The elapsed time as a <code>Duration</code>
     */
    public Duration getDuration() {
        return new Duration(getElapsedTimeMillis());
    }

    /**
     * Returns the calculated throughput of processing <code>num</code> items
     * using this SimpleTimer to provide the elapsed processing time. The
     * throughput is calculated in items per second.
     *
     * @param num  The number of items processed.
     * @return  The throughput in items per second.
     */
    public double getThroughput(int num) {
        long elapsed = getElapsedTimeMillis();
        if (elapsed == 0) return 0;
        return num / (elapsed / 1000.0);
    }

    /**
     * Returns the mean (average) latency of processing <code>num</code> items.
     * The mean latency is calculated in milliseconds per item.
     * @param num  The number of items that have been processed.
     * @return  The mean latency in milliseconds per item.
     */
    public double getMeanLatencyInMillis(int num) {
        if (num == 0) return 0.0;
        return ((double) getElapsedTimeMillis()) / num;
    }

    @Override
    public String toString() {
        return durationToString(getDuration());
    }

    protected static String durationToString(Duration duration) {
        String formatted = formatter.print(duration.toPeriod());
        return formatted;
    }
}
