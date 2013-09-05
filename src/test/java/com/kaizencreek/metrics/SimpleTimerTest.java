package com.kaizencreek.metrics;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SimpleTimerTest {

	private static final double TOLERANCE_FACTOR = 0.10;	// 10%

    private Random randomGenerator = new Random(System.currentTimeMillis());

	@Test
	public void testUnstartedTimer() {

		// given: "A SimpleTimer that has not been started"
		SimpleTimer simpleTimer = new SimpleTimer();
		Assert.assertEquals(0, simpleTimer.getStartTime());
		Assert.assertEquals(0, simpleTimer.getEndTime());
		Assert.assertEquals(0, simpleTimer.getElapsedTimeMillis());
		Assert.assertEquals(0, simpleTimer.getDuration().getMillis());
		Assert.assertEquals(0, simpleTimer.getMeanLatencyInMillis(0), 0);
		Assert.assertEquals(0, simpleTimer.getThroughput(0), 0);
	}

	@Test
	public void testStartedTimer() {

		// given: "A SimpleTimer that has been started"
		SimpleTimer simpleTimer = new SimpleTimer().start();

		// expect: "getStartTime to return non-zero positive start time"
		Assert.assertTrue("getStartTime should be a non-zero positive value", simpleTimer.getStartTime() > 0);
	}

	@Test
	public void testEndedTimer() throws InterruptedException {

		// given: "A SimpleTimer that has ended after 200ms"
		SimpleTimer simpleTimer = new SimpleTimer().start();
		long sleepTime = 200L;
		Thread.sleep(sleepTime);
		simpleTimer.end();

		// expect: "An elapsed time of approximately 200ms"
		double tolerance = sleepTime * TOLERANCE_FACTOR;
		Assert.assertEquals(sleepTime, simpleTimer.getEndTime() - simpleTimer.getStartTime(), tolerance);
		Assert.assertEquals(sleepTime, simpleTimer.getElapsedTimeMillis(),    tolerance);
		Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
	}

	@Test
	public void testReset() throws InterruptedException {
        // given: "A SimpleTimer that has ended after 200ms"
        SimpleTimer simpleTimer = new SimpleTimer().start();
        long sleepTime = 200L;
        Thread.sleep(sleepTime);
        simpleTimer.end();

        // expect: "An elapsed time of approximately 200ms"
        double tolerance = sleepTime * TOLERANCE_FACTOR;
        Assert.assertEquals(sleepTime, simpleTimer.getEndTime() - simpleTimer.getStartTime(), tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getElapsedTimeMillis(),    tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
	}

    @Test
    public void testThroughputAndMeanLatency() throws InterruptedException {
        // given: "A SimpleTimer that has been started"
        SimpleTimer simpleTimer = new SimpleTimer().start();

        // when: "Some amount of work has been done"
        int numTasks = 10;
        int meanLatency = 50;  // 50ms
        int delta = 5;         // 5ms
        doSomeWork(simpleTimer, numTasks, meanLatency - delta, meanLatency + delta);

        // then: "We should be able to measure the throughput"
        System.out.println(String.format("Throughput = %s", simpleTimer.getThroughput(numTasks)));
        double expectedThroughput = 1000.0 / meanLatency;       // throughput = 1 / meanLatency (converted to seconds)
        double tolerance = expectedThroughput * TOLERANCE_FACTOR;
        Assert.assertEquals(simpleTimer.getThroughput(numTasks), expectedThroughput, tolerance);
        Assert.assertEquals(simpleTimer.getMeanLatencyInMillis(numTasks), meanLatency, delta);
    }

        @Test
        public void testGetETA() throws InterruptedException {
        // given: "A SimpleTimer that has been started"
        SimpleTimer simpleTimer = new SimpleTimer().start();

        // when: "Some amount of work has been done"
        int numTasks = 50;
        int totalTasks = 500;
        int meanLatency = 20;  // 20ms
        int delta = 5;         // 5ms
        doSomeWork(simpleTimer, numTasks, meanLatency - delta, meanLatency + delta);

        // then: "We should get a good estimate for the ETA"
        double timeRemaining = (totalTasks - numTasks) * meanLatency;
        double tolerance = timeRemaining * TOLERANCE_FACTOR;
        Duration eta = simpleTimer.getETA(numTasks, totalTasks);
        System.out.println("eta = " + eta);
            Assert.assertEquals(eta.getMillis(), timeRemaining, tolerance);

        DateTime etaTime = simpleTimer.getETATime(numTasks, totalTasks);
        long currentMillis = System.currentTimeMillis();
        System.out.println("etaTime = " + etaTime);
        Assert.assertEquals(etaTime.getMillis(), currentMillis + timeRemaining, tolerance);
    }

    protected void doSomeWork(SimpleTimer timer, int numTasks, int minTaskTime, int maxTaskTime) throws InterruptedException {
        for (int n = 0; n < numTasks; n++) {
            // uniform random numbers between minTaskTime and maxTaskTime
            int randomWaitMillis = minTaskTime + randomGenerator.nextInt(maxTaskTime - minTaskTime);
            Thread.sleep(randomWaitMillis);
        }
    }

}
